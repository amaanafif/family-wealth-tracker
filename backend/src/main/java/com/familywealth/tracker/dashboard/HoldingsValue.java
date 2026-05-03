package com.familywealth.tracker.dashboard;

import java.math.BigDecimal;
import java.util.UUID;

public record HoldingsValue(
    UUID id,
    String name,
    String type,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal value,
    BigDecimal investedValue,
    BigDecimal profitLoss
) {
}
