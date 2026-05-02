package com.familywealth.tracker.price;

import com.familywealth.tracker.asset.Asset;
import java.math.BigDecimal;
import java.util.Optional;

public interface PriceClient {
    Optional<BigDecimal> fetchPrice(Asset asset);
    String source();
}
