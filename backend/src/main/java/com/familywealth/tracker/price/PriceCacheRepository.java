package com.familywealth.tracker.price;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCacheRepository extends JpaRepository<PriceCache, String> {
}
