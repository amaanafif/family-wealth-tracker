package com.familywealth.tracker.portfolio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "portfolio_summary")
public class PortfolioSummary {
    @Id
    private String type;

    private BigDecimal totalInvested;

    private BigDecimal totalCurrent;

    private BigDecimal totalPnl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public BigDecimal getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(BigDecimal totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public BigDecimal getTotalPnl() {
        return totalPnl;
    }

    public void setTotalPnl(BigDecimal totalPnl) {
        this.totalPnl = totalPnl;
    }
}
