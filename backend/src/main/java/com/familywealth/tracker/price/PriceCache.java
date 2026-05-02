package com.familywealth.tracker.price;

import com.familywealth.tracker.asset.AssetType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class PriceCache {
    @Id
    private String cacheKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AssetType type;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    private Instant fetchedAt;
    private String source;

    protected PriceCache() {
    }

    public PriceCache(String cacheKey, AssetType type, BigDecimal price, Instant fetchedAt, String source) {
        this.cacheKey = cacheKey;
        this.type = type;
        this.price = price;
        this.fetchedAt = fetchedAt;
        this.source = source;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public AssetType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public String getSource() {
        return source;
    }
}
