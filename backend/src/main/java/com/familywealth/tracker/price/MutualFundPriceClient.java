package com.familywealth.tracker.price;

import com.familywealth.tracker.portfolio.Holdings;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MutualFundPriceClient implements PriceClient {
    private final RestClient restClient = RestClient.create("https://api.mfapi.in");

    @Override
    public Optional<BigDecimal> fetchPrice(Holdings holding) {
        if (holding.getSymbol() == null || holding.getSymbol().isBlank()) {
            return Optional.empty();
        }

        try {
            MfApiResponse response = restClient.get()
                .uri("/mf/{schemeCode}/latest", holding.getSymbol())
                .retrieve()
                .body(MfApiResponse.class);
            if (response != null && response.data() != null && response.data().nav() != null) {
                return Optional.of(new BigDecimal(response.data().nav()));
            }
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public String source() {
        return "AMFI NAV via mfapi.in";
    }

    record MfApiResponse(MfNav data) {
    }

    record MfNav(String nav) {
    }
}
