package com.kajal.saas.repo;

import com.kajal.saas.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByOrganizationId(Long organizationId);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
