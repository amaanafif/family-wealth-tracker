package com.familywealth.tracker.asset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @Transient
    private String symbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "asset_type")
    private AssetType type;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal value = BigDecimal.ZERO;

    private UUID ownerId;
    private UUID familyId;

    @DecimalMin("0.0")
    private BigDecimal largePct = BigDecimal.ZERO;

    @DecimalMin("0.0")
    private BigDecimal midPct = BigDecimal.ZERO;

    @DecimalMin("0.0")
    private BigDecimal smallPct = BigDecimal.ZERO;

    @DecimalMin("0.0")
    private BigDecimal foreignPct = BigDecimal.ZERO;

    @DecimalMin("0.0")
    private BigDecimal debtPct = BigDecimal.ZERO;

    @Transient
    private AllocationBucket bucket = AllocationBucket.OTHER;

    @Transient
    @DecimalMin("0.0")
    private BigDecimal quantity = BigDecimal.ONE;

    @Transient
    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public AllocationBucket getBucket() {
        return dominantBucket();
    }

    public void setBucket(AllocationBucket bucket) {
        this.bucket = bucket;
        applyBucket(bucket);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value == null ? BigDecimal.ZERO : value;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public BigDecimal getLargePct() {
        return largePct;
    }

    public void setLargePct(BigDecimal largePct) {
        this.largePct = safe(largePct);
    }

    public BigDecimal getMidPct() {
        return midPct;
    }

    public void setMidPct(BigDecimal midPct) {
        this.midPct = safe(midPct);
    }

    public BigDecimal getSmallPct() {
        return smallPct;
    }

    public void setSmallPct(BigDecimal smallPct) {
        this.smallPct = safe(smallPct);
    }

    public BigDecimal getForeignPct() {
        return foreignPct;
    }

    public void setForeignPct(BigDecimal foreignPct) {
        this.foreignPct = safe(foreignPct);
    }

    public BigDecimal getDebtPct() {
        return debtPct;
    }

    public void setDebtPct(BigDecimal debtPct) {
        this.debtPct = safe(debtPct);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? BigDecimal.ONE : quantity;
    }

    public BigDecimal getManualPrice() {
        return value;
    }

    public void setManualPrice(BigDecimal manualPrice) {
        this.value = manualPrice == null ? BigDecimal.ZERO : manualPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void touch() {
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    public BigDecimal bucketValue(AllocationBucket allocationBucket) {
        BigDecimal pct = switch (allocationBucket) {
            case LARGE_CAP -> largePct;
            case MID_CAP -> midPct;
            case SMALL_CAP -> smallPct;
            case FOREIGN -> foreignPct;
            case DEBT_CASH -> debtPct;
            case CRYPTO -> type == AssetType.CRYPTO ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
            case REAL_ESTATE -> type == AssetType.REAL_ESTATE ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
            case OTHER -> BigDecimal.ZERO;
        };
        return value.multiply(safe(pct)).divide(BigDecimal.valueOf(100));
    }

    private AllocationBucket dominantBucket() {
        if (type == AssetType.CRYPTO) {
            return AllocationBucket.CRYPTO;
        }
        if (type == AssetType.REAL_ESTATE) {
            return AllocationBucket.REAL_ESTATE;
        }
        AllocationBucket result = bucket == null ? AllocationBucket.OTHER : bucket;
        BigDecimal max = BigDecimal.ZERO;
        if (safe(largePct).compareTo(max) > 0) {
            result = AllocationBucket.LARGE_CAP;
            max = safe(largePct);
        }
        if (safe(midPct).compareTo(max) > 0) {
            result = AllocationBucket.MID_CAP;
            max = safe(midPct);
        }
        if (safe(smallPct).compareTo(max) > 0) {
            result = AllocationBucket.SMALL_CAP;
            max = safe(smallPct);
        }
        if (safe(foreignPct).compareTo(max) > 0) {
            result = AllocationBucket.FOREIGN;
            max = safe(foreignPct);
        }
        if (safe(debtPct).compareTo(max) > 0) {
            result = AllocationBucket.DEBT_CASH;
        }
        return result;
    }

    private void applyBucket(AllocationBucket bucket) {
        largePct = BigDecimal.ZERO;
        midPct = BigDecimal.ZERO;
        smallPct = BigDecimal.ZERO;
        foreignPct = BigDecimal.ZERO;
        debtPct = BigDecimal.ZERO;
        if (bucket == null) {
            return;
        }
        switch (bucket) {
            case LARGE_CAP -> largePct = BigDecimal.valueOf(100);
            case MID_CAP -> midPct = BigDecimal.valueOf(100);
            case SMALL_CAP -> smallPct = BigDecimal.valueOf(100);
            case FOREIGN -> foreignPct = BigDecimal.valueOf(100);
            case DEBT_CASH -> debtPct = BigDecimal.valueOf(100);
            default -> {
            }
        }
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
