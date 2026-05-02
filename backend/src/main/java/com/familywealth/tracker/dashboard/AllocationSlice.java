package com.familywealth.tracker.dashboard;

import java.math.BigDecimal;

public record AllocationSlice(String label, BigDecimal value, BigDecimal percentage) {
}
