package com.familywealth.tracker.portfolio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolio_summary")
@Getter
@Setter
public class PortfolioSummary {

    @Id
    private String type; // e.g. ALL, STOCK, MF

    @Column(name = "total_invested")
    private BigDecimal totalInvested;

    @Column(name = "total_current")
    private BigDecimal totalCurrent;

    @Column(name = "total_pnl")
    private BigDecimal totalPnl;
}