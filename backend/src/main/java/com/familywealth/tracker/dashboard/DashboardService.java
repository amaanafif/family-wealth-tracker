package com.familywealth.tracker.dashboard;

import com.familywealth.tracker.asset.AllocationBucket;
import com.familywealth.tracker.asset.Asset;
import com.familywealth.tracker.asset.AssetRepository;
import com.familywealth.tracker.asset.AssetType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final AssetRepository assets;

    public DashboardService(AssetRepository assets) {
        this.assets = assets;
    }

    public DashboardSummary summary() {
        List<Asset> assetList = assets.findAll();
        List<AssetValue> assetValues = assetList.stream()
            .map(asset -> new AssetValue(
                asset.getId(),
                asset.getName(),
                asset.getType(),
                asset.getBucket(),
                BigDecimal.ONE,
                asset.getValue(),
                asset.getValue()
            ))
            .toList();

        BigDecimal totalAssets = assetValues.stream()
            .map(AssetValue::value)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(
            totalAssets,
            totalAssets,
            allocationByType(assetValues, totalAssets),
            allocationByBucket(assetList, totalAssets),
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

    private List<AllocationSlice> allocationByBucket(List<Asset> assetList, BigDecimal totalAssets) {
        return assetList.stream()
            .flatMap(asset -> List.of(
                slice("Large Cap", asset.bucketValue(AllocationBucket.LARGE_CAP), totalAssets),
                slice("Mid Cap", asset.bucketValue(AllocationBucket.MID_CAP), totalAssets),
                slice("Small Cap", asset.bucketValue(AllocationBucket.SMALL_CAP), totalAssets),
                slice("Foreign", asset.bucketValue(AllocationBucket.FOREIGN), totalAssets),
                slice("Crypto", asset.bucketValue(AllocationBucket.CRYPTO), totalAssets),
                slice("Real Estate", asset.bucketValue(AllocationBucket.REAL_ESTATE), totalAssets),
                slice("Debt / Cash", asset.bucketValue(AllocationBucket.DEBT_CASH), totalAssets)
            ).stream())
            .collect(Collectors.groupingBy(AllocationSlice::label, Collectors.reducing(BigDecimal.ZERO, AllocationSlice::value, BigDecimal::add)))
            .entrySet()
            .stream()
            .map(entry -> slice(entry.getKey(), entry.getValue(), totalAssets))
            .filter(slice -> slice.value().signum() > 0)
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
            case MF -> "Mutual Funds";
            case CRYPTO -> "Crypto";
            case REAL_ESTATE -> "Real Estate";
            case FD, CASH -> "Debt / Cash";
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
