package com.familywealth.tracker.price;

import com.familywealth.tracker.portfolio.Holdings;
import java.math.BigDecimal;
import java.util.Optional;

public interface PriceClient {
    Optional<BigDecimal> fetchPrice(Holdings holding);
    String source();
}
