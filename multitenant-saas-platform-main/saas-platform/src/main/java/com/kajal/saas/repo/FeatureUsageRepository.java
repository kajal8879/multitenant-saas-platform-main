package com.kajal.saas.repo;

import com.kajal.saas.entity.FeatureUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureUsageRepository extends JpaRepository<FeatureUsage, Long> {
    Optional<FeatureUsage> findByOrganizationIdAndFeatureKey(Long organizationId, String featureKey);
}
