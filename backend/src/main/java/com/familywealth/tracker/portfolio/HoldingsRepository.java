package com.familywealth.tracker.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HoldingsRepository extends JpaRepository<Holdings, UUID> {
}