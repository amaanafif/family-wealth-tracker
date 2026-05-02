package com.familywealth.tracker.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummary(
    BigDecimal totalAssets,
    BigDecimal totalLiabilities,
    BigDecimal netWorth,
    List<AllocationSlice> allocationByType,
    List<AllocationSlice> lookThroughAllocation,
    List<AssetValue> holdings
) {
}
