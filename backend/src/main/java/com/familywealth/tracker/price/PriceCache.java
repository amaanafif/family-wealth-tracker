package com.familywealth.tracker.price;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "prices")
public class PriceCache {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String symbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "price_type")
    private PriceType type;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal value;

    private Instant lastUpdated;

    protected PriceCache() {
    }

    public PriceCache(String symbol, PriceType type, BigDecimal value, Instant lastUpdated) {
        this.symbol = symbol;
        this.type = type;
        this.value = value;
        this.lastUpdated = lastUpdated;
    }

    public UUID getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public PriceType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return value;
    }

    public Instant getFetchedAt() {
        return lastUpdated;
    }
}
