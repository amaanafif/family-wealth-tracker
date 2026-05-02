package com.familywealth.tracker.asset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String symbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AssetType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AllocationBucket bucket;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal quantity = BigDecimal.ONE;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal manualPrice = BigDecimal.ZERO;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Long getId() {
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
        return bucket;
    }

    public void setBucket(AllocationBucket bucket) {
        this.bucket = bucket;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getManualPrice() {
        return manualPrice;
    }

    public void setManualPrice(BigDecimal manualPrice) {
        this.manualPrice = manualPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void touch() {
        this.updatedAt = Instant.now();
    }
}
