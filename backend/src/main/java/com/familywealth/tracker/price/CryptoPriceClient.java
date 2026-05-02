package com.familywealth.tracker.price;

import com.familywealth.tracker.asset.Asset;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CryptoPriceClient implements PriceClient {
    private final RestClient restClient = RestClient.create();

    @Override
    public Optional<BigDecimal> fetchPrice(Asset asset) {
        if (asset.getSymbol() == null || asset.getSymbol().isBlank()) {
            return Optional.empty();
        }

        try {
            Map<?, ?> response = restClient.get()
                .uri("https://api.coingecko.com/api/v3/simple/price?ids={id}&vs_currencies=inr", asset.getSymbol())
                .retrieve()
                .body(Map.class);
            Map<?, ?> quote = (Map<?, ?>) response.get(asset.getSymbol());
            Object price = quote.get("inr");
            if (price instanceof Number number) {
                return Optional.of(BigDecimal.valueOf(number.doubleValue()));
            }
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public String source() {
        return "CoinGecko";
    }
}
