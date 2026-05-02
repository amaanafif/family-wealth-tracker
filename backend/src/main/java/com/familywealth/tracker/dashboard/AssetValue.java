package com.familywealth.tracker.dashboard;

import com.familywealth.tracker.asset.AllocationBucket;
import com.familywealth.tracker.asset.AssetType;
import java.math.BigDecimal;

public record AssetValue(
    Long id,
    String name,
    AssetType type,
    AllocationBucket bucket,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal value
) {
}
