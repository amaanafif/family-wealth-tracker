package com.familywealth.tracker.price;

import com.familywealth.tracker.portfolio.Holdings;
import com.familywealth.tracker.portfolio.HoldingsRepository;
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
    private final HoldingsRepository holdings;
    private final PriceCacheRepository priceCaches;
    private final StockPriceClient stockClient;
    private final MutualFundPriceClient mutualFundClient;

    public PriceService(
        HoldingsRepository holdings,
        PriceCacheRepository priceCaches,
        StockPriceClient stockClient,
        MutualFundPriceClient mutualFundClient
    ) {
        this.holdings = holdings;
        this.priceCaches = priceCaches;
        this.stockClient = stockClient;
        this.mutualFundClient = mutualFundClient;
    }

    public BigDecimal priceFor(Holdings holding) {
        if (!isMarketPriced(holding.getType())) {
            return holding.getCurrentValue(); // or something
        }

        if (holding.getSymbol() == null || holding.getSymbol().isBlank()) {
            return holding.getCurrentValue();
        }

        return priceCaches.findBySymbolAndType(holding.getSymbol(), priceType(holding.getType()))
            .map(PriceCache::getPrice)
            .orElse(holding.getCurrentValue());
    }

    public Map<String, Integer> refreshStalePrices(boolean force) {
        List<Holdings> marketHoldings = holdings.findAll().stream()
            .filter(h -> List.of("STOCK", "MF").contains(h.getType()))
            .toList();
        int refreshed = 0;
        int skipped = 0;

        for (Holdings holding : marketHoldings) {
            if (holding.getSymbol() == null || holding.getSymbol().isBlank()) {
                skipped++;
                continue;
            }
            if (!force && !isStale(holding)) {
                skipped++;
                continue;
            }
            Optional<PriceClient> client = clientFor(holding.getType());
            if (client.isEmpty()) {
                skipped++;
                continue;
            }
            Optional<BigDecimal> price = client.get().fetchPrice(holding);
            if (price.isPresent()) {
                priceCaches.save(new PriceCache(holding.getSymbol(), priceType(holding.getType()), price.get(), Instant.now()));
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

    private boolean isStale(Holdings holding) {
        return priceCaches.findBySymbolAndType(holding.getSymbol(), priceType(holding.getType()))
            .map(cache -> cache.getFetchedAt().plus(frequency(holding.getType())).isBefore(Instant.now()))
            .orElse(true);
    }

    private Duration frequency(String type) {
        return switch (type) {
            case "STOCK" -> Duration.ofHours(1);
            case "MF" -> Duration.ofDays(1);
            default -> Duration.ofDays(365);
        };
    }

    private boolean isMarketPriced(String type) {
        return "STOCK".equals(type) || "MF".equals(type);
    }

    private Optional<PriceClient> clientFor(String type) {
        return switch (type) {
            case "STOCK" -> Optional.of(stockClient);
            case "MF" -> Optional.of(mutualFundClient);
            default -> Optional.empty();
        };
    }

    private PriceType priceType(String type) {
        return switch (type) {
            case "STOCK" -> PriceType.STOCK;
            case "MF" -> PriceType.MF;
            default -> throw new IllegalArgumentException("Unsupported price type: " + type);
        };
    }
}
