package com.familywealth.tracker.dashboard;

import com.familywealth.tracker.asset.AllocationBucket;
import com.familywealth.tracker.asset.Asset;
import com.familywealth.tracker.asset.AssetRepository;
import com.familywealth.tracker.asset.AssetType;
import com.familywealth.tracker.liability.LiabilityRepository;
import com.familywealth.tracker.price.PriceService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final AssetRepository assets;
    private final LiabilityRepository liabilities;
    private final PriceService prices;

    public DashboardService(AssetRepository assets, LiabilityRepository liabilities, PriceService prices) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.prices = prices;
    }

    public DashboardSummary summary() {
        List<AssetValue> assetValues = assets.findAll().stream()
            .map(asset -> new AssetValue(
                asset.getId(),
                asset.getName(),
                asset.getType(),
                asset.getBucket(),
                asset.getQuantity(),
                prices.priceFor(asset),
                asset.getQuantity().multiply(prices.priceFor(asset))
            ))
            .toList();

        BigDecimal totalAssets = assetValues.stream()
            .map(AssetValue::value)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLiabilities = liabilities.findAll().stream()
            .map(liability -> liability.getOutstandingAmount() == null ? BigDecimal.ZERO : liability.getOutstandingAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(
            totalAssets,
            totalLiabilities,
            totalAssets.subtract(totalLiabilities),
            allocationByType(assetValues, totalAssets),
            allocationByBucket(assetValues, totalAssets),
            assetValues.stream().sorted(Comparator.comparing(AssetValue::value).reversed()).toList()
        );
    }

    private List<AllocationSlice> allocationByType(List<AssetValue> values, BigDecimal totalAssets) {
        return values.stream()
            .collect(Collectors.groupingBy(value -> label(value.type()), Collectors.reducing(BigDecimal.ZERO, AssetValue::value, BigDecimal::add)))
            .entrySet()
            .stream()
            .map(entry -> slice(entry.getKey(), entry.getValue(), totalAssets))
            .sorted(Comparator.comparing(AllocationSlice::value).reversed())
            .toList();
    }

    private List<AllocationSlice> allocationByBucket(List<AssetValue> values, BigDecimal totalAssets) {
        return values.stream()
            .collect(Collectors.groupingBy(value -> label(value.bucket()), Collectors.reducing(BigDecimal.ZERO, AssetValue::value, BigDecimal::add)))
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

    private String label(AssetType type) {
        return switch (type) {
            case STOCK -> "Equity";
            case MUTUAL_FUND -> "Mutual Funds";
            case CRYPTO -> "Crypto";
            case REAL_ESTATE -> "Real Estate";
            case FIXED_DEPOSIT, CASH -> "Debt / Cash";
            case OTHER -> "Other";
        };
    }

    private String label(AllocationBucket bucket) {
        return switch (bucket) {
            case LARGE_CAP -> "Large Cap";
            case MID_CAP -> "Mid Cap";
            case SMALL_CAP -> "Small Cap";
            case FOREIGN -> "Foreign";
            case CRYPTO -> "Crypto";
            case REAL_ESTATE -> "Real Estate";
            case DEBT_CASH -> "Debt / Cash";
            case OTHER -> "Other";
        };
    }
}
