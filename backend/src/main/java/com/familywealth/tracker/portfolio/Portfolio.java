package com.familywealth.tracker.portfolio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
public class Portfolio {

    @Id
    private String symbol;

    private String name;
    private String isin;
    private String type;
    private String sector;

    @Column(name = "scheme_type")
    private String schemeType;

    private BigDecimal quantity;

    @Column(name = "avg_price")
    private BigDecimal avgPrice;

    @Column(name = "last_price")
    private BigDecimal lastPrice;

    @Column(name = "invested_value")
    private BigDecimal investedValue;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    private BigDecimal pnl;

    @Column(name = "pnl_pct")
    private BigDecimal pnlPct;
}