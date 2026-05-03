package com.familywealth.tracker.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HoldingsRepository extends JpaRepository<Holdings, UUID> {
    List<Holdings> findByOwnerId(UUID ownerId);
}
