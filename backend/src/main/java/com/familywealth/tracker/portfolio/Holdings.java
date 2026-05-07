package com.familywealth.tracker.portfolio;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "holdings")
public class Holdings {

    @Id
    private UUID id;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    private String symbol;
    private String isin;
    private String type;
    private String sector;
    private String schemeType;

    private BigDecimal quantity;
    private BigDecimal quantityLongTerm;
    private BigDecimal quantityPledgedMargin;
    private BigDecimal quantityPledgedLoan;

    private BigDecimal avgPrice;
    private BigDecimal lastPrice;
    private BigDecimal investedValue;
    private BigDecimal currentValue;
    private BigDecimal pnl;
    private BigDecimal pnlPct;

    private Instant createdAt;
    private Instant updatedAt;
}