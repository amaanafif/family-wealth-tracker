package com.familywealth.tracker.price;

import com.familywealth.tracker.asset.Asset;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StockPriceClient implements PriceClient {
    private final RestClient restClient = RestClient.create();

    @Override
    public Optional<BigDecimal> fetchPrice(Asset asset) {
        if (asset.getSymbol() == null || asset.getSymbol().isBlank()) {
            return Optional.empty();
        }

        try {
            Map<?, ?> response = restClient.get()
                .uri("https://query1.finance.yahoo.com/v8/finance/chart/{symbol}", asset.getSymbol())
                .retrieve()
                .body(Map.class);
            Map<?, ?> chart = (Map<?, ?>) response.get("chart");
            var result = (java.util.List<?>) chart.get("result");
            Map<?, ?> meta = (Map<?, ?>) ((Map<?, ?>) result.get(0)).get("meta");
            Object price = meta.get("regularMarketPrice");
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
        return "Yahoo Finance";
    }
}
