package com.familywealth.tracker.price;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "prices")
@Getter
@Setter
public class Prices {

    @Id
    private UUID id;

    private String symbol;

    private String type;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @PrePersist
    public void prePersist() {
        this.id = (this.id == null) ? UUID.randomUUID() : this.id;
        this.lastUpdated = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = Instant.now();
    }
}