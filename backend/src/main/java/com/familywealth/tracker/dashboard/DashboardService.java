package com.familywealth.tracker.dashboard;

import com.familywealth.tracker.portfolio.Holdings;
import com.familywealth.tracker.portfolio.HoldingsRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final HoldingsRepository holdings;

    public DashboardService(HoldingsRepository holdings) {
        this.holdings = holdings;
    }

    public DashboardSummary summary() {
        List<Holdings> holdingsList = holdings.findAll();
        List<HoldingsValue> holdingsValues = holdingsList.stream()
            .map(holding -> new HoldingsValue(
                holding.getId(),
                holding.getName(),
                holding.getType(),
                holding.getQuantity(),
                holding.getLastPrice(),
                holding.getCurrentValue(),
                holding.getInvestedValue(),
                holding.getPnl()
            ))
            .toList();

        BigDecimal totalAssets = holdingsValues.stream()
            .map(HoldingsValue::value)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInvested = holdingsValues.stream()
            .map(HoldingsValue::investedValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfitLoss = holdingsValues.stream()
            .map(HoldingsValue::profitLoss)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(
            totalAssets,
            totalAssets,
            totalInvested,
            totalProfitLoss,
            allocationByType(holdingsValues, totalAssets),
            List.of(), // remove lookThroughAllocation
            holdingsValues.stream().sorted(Comparator.comparing(HoldingsValue::value).reversed()).toList()
        );
    }

    private List<AllocationSlice> allocationByType(List<HoldingsValue> values, BigDecimal totalAssets) {
        return values.stream()
            .collect(Collectors.groupingBy(value -> label(value.type()), Collectors.reducing(BigDecimal.ZERO, HoldingsValue::value, BigDecimal::add)))
            .entrySet()
            .stream()
            .map(entry -> slice(entry.getKey(), entry.getValue(), totalAssets))
            .sorted(Comparator.comparing(AllocationSlice::value).reversed())
            .toList();
    }

    private AllocationSlice slice(String label, BigDecimal value, BigDecimal totalAssets) {
        BigDecimal percentage = totalAssets.signum() == 0
            ? BigDecimal.ZERO
            : value.multiply(BigDecimal.valueOf(100)).divide(totalAssets, 2, RoundingMode.HALF_UP);
        return new AllocationSlice(label, value, percentage);
    }

    private String label(String type) {
        return switch (type) {
            case "STOCK" -> "Equity";
            case "MF" -> "Mutual Funds";
            default -> type;
        };
    }
}
