package com.familywealth.tracker.portfolio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "holdings")
public class Holdings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID ownerId;

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

    private String name;

    private String symbol;

    private String isin;

    private String type;

    private String sector;

    private String schemeType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityLongTerm() {
        return quantityLongTerm;
    }

    public void setQuantityLongTerm(BigDecimal quantityLongTerm) {
        this.quantityLongTerm = quantityLongTerm;
    }

    public BigDecimal getQuantityPledgedMargin() {
        return quantityPledgedMargin;
    }

    public void setQuantityPledgedMargin(BigDecimal quantityPledgedMargin) {
        this.quantityPledgedMargin = quantityPledgedMargin;
    }

    public BigDecimal getQuantityPledgedLoan() {
        return quantityPledgedLoan;
    }

    public void setQuantityPledgedLoan(BigDecimal quantityPledgedLoan) {
        this.quantityPledgedLoan = quantityPledgedLoan;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getInvestedValue() {
        return investedValue;
    }

    public void setInvestedValue(BigDecimal investedValue) {
        this.investedValue = investedValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getPnl() {
        return pnl;
    }

    public void setPnl(BigDecimal pnl) {
        this.pnl = pnl;
    }

    public BigDecimal getPnlPct() {
        return pnlPct;
    }

    public void setPnlPct(BigDecimal pnlPct) {
        this.pnlPct = pnlPct;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }
}
