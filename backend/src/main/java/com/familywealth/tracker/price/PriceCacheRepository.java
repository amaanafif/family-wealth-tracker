package com.familywealth.tracker.price;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCacheRepository extends JpaRepository<PriceCache, UUID> {
    Optional<PriceCache> findBySymbolAndType(String symbol, PriceType type);
}
