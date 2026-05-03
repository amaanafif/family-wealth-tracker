package com.familywealth.tracker.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummary(
    BigDecimal totalAssets,
    BigDecimal netWorth,
    BigDecimal totalInvested,
    BigDecimal totalProfitLoss,
    List<AllocationSlice> allocationByType,
    List<AllocationSlice> lookThroughAllocation,
    List<HoldingsValue> holdings
) {
}
