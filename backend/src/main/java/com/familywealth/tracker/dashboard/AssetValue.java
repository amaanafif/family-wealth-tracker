package com.familywealth.tracker.dashboard;

import com.familywealth.tracker.asset.AllocationBucket;
import com.familywealth.tracker.asset.AssetType;
import java.math.BigDecimal;
import java.util.UUID;

public record AssetValue(
    UUID id,
    String name,
    AssetType type,
    AllocationBucket bucket,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal value
) {
}
