package com.familywealth.tracker.price;

import com.familywealth.tracker.asset.Asset;
import com.familywealth.tracker.asset.AssetRepository;
import com.familywealth.tracker.asset.AssetType;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final AssetRepository assets;
    private final PriceCacheRepository priceCaches;
    private final StockPriceClient stockClient;
    private final CryptoPriceClient cryptoClient;
    private final MutualFundPriceClient mutualFundClient;

    public PriceService(
        AssetRepository assets,
        PriceCacheRepository priceCaches,
        StockPriceClient stockClient,
        CryptoPriceClient cryptoClient,
        MutualFundPriceClient mutualFundClient
    ) {
        this.assets = assets;
        this.priceCaches = priceCaches;
        this.stockClient = stockClient;
        this.cryptoClient = cryptoClient;
        this.mutualFundClient = mutualFundClient;
    }

    public BigDecimal priceFor(Asset asset) {
        if (!isMarketPriced(asset.getType())) {
            return asset.getValue();
        }

        if (asset.getSymbol() == null || asset.getSymbol().isBlank()) {
            return asset.getValue();
        }

        return priceCaches.findBySymbolAndType(asset.getSymbol(), priceType(asset.getType()))
            .map(PriceCache::getPrice)
            .orElse(asset.getValue());
    }

    public Map<String, Integer> refreshStalePrices(boolean force) {
        List<Asset> marketAssets = assets.findByTypeIn(List.of(AssetType.STOCK, AssetType.CRYPTO, AssetType.MF));
        int refreshed = 0;
        int skipped = 0;

        for (Asset asset : marketAssets) {
            if (asset.getSymbol() == null || asset.getSymbol().isBlank()) {
                skipped++;
                continue;
            }
            if (!force && !isStale(asset)) {
                skipped++;
                continue;
            }
            Optional<PriceClient> client = clientFor(asset.getType());
            if (client.isEmpty()) {
                skipped++;
                continue;
            }
            Optional<BigDecimal> price = client.get().fetchPrice(asset);
            if (price.isPresent()) {
                priceCaches.save(new PriceCache(asset.getSymbol(), priceType(asset.getType()), price.get(), Instant.now()));
                refreshed++;
            } else {
                skipped++;
            }
        }

        return Map.of("refreshed", refreshed, "skipped", skipped);
    }

    @Scheduled(cron = "0 15 6 * * *")
    void scheduledRefresh() {
        refreshStalePrices(false);
    }

    private boolean isStale(Asset asset) {
        return priceCaches.findBySymbolAndType(asset.getSymbol(), priceType(asset.getType()))
            .map(cache -> cache.getFetchedAt().plus(frequency(asset.getType())).isBefore(Instant.now()))
            .orElse(true);
    }

    private Duration frequency(AssetType type) {
        return switch (type) {
            case CRYPTO -> Duration.ofMinutes(15);
            case STOCK -> Duration.ofHours(1);
            case MF -> Duration.ofDays(1);
            default -> Duration.ofDays(365);
        };
    }

    private boolean isMarketPriced(AssetType type) {
        return type == AssetType.STOCK || type == AssetType.CRYPTO || type == AssetType.MF;
    }

    private Optional<PriceClient> clientFor(AssetType type) {
        return switch (type) {
            case STOCK -> Optional.of(stockClient);
            case CRYPTO -> Optional.of(cryptoClient);
            case MF -> Optional.of(mutualFundClient);
            default -> Optional.empty();
        };
    }

    private PriceType priceType(AssetType type) {
        return switch (type) {
            case STOCK -> PriceType.STOCK;
            case CRYPTO -> PriceType.CRYPTO;
            case MF -> PriceType.MF;
            default -> throw new IllegalArgumentException("Unsupported price type: " + type);
        };
    }
}
