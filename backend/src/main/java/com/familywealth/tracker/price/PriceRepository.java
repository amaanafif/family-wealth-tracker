package com.familywealth.tracker.price;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PriceRepository extends JpaRepository<Prices, UUID> {
    Optional<Prices> findBySymbolAndType(String symbol, String type);
}
