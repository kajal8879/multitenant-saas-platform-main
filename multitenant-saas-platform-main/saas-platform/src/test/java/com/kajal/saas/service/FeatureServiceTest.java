package com.kajal.saas.service;

import com.kajal.saas.entity.*;
import com.kajal.saas.repo.FeatureUsageRepository;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.SubscriptionRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FeatureServiceTest {
    private final SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private final FeatureUsageRepository usageRepository = mock(FeatureUsageRepository.class);
    private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    private final FeatureService featureService = new FeatureService(subscriptionRepository, usageRepository, organizationRepository);

    @Test
    void freePlanCannotUseProFeature() {
        Subscription sub = new Subscription();
        sub.setPlan(PlanType.FREE);
        sub.setStatus(SubscriptionStatus.ACTIVE);

        when(subscriptionRepository.findByOrganizationId(1L)).thenReturn(Optional.of(sub));
        when(usageRepository.findByOrganizationIdAndFeatureKey(1L, "advanced_reports")).thenReturn(Optional.empty());

        var answer = featureService.check(1L, "advanced_reports");

        assertThat(answer.allowed()).isFalse();
        assertThat(answer.reason()).isEqualTo("Feature is not in plan");
    }

    @Test
    void activeProPlanCanUseAdvancedReports() {
        Subscription sub = new Subscription();
        sub.setPlan(PlanType.PRO);
        sub.setStatus(SubscriptionStatus.ACTIVE);

        when(subscriptionRepository.findByOrganizationId(1L)).thenReturn(Optional.of(sub));
        when(usageRepository.findByOrganizationIdAndFeatureKey(1L, "advanced_reports")).thenReturn(Optional.empty());

        var answer = featureService.check(1L, "advanced_reports");

        assertThat(answer.allowed()).isTrue();
        assertThat(answer.limit()).isEqualTo(100);
    }

    @Test
    void limitStopsAccess() {
        Subscription sub = new Subscription();
        sub.setPlan(PlanType.FREE);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        FeatureUsage usage = new FeatureUsage();
        usage.setUsedCount(10);

        when(subscriptionRepository.findByOrganizationId(1L)).thenReturn(Optional.of(sub));
        when(usageRepository.findByOrganizationIdAndFeatureKey(1L, "basic_reports")).thenReturn(Optional.of(usage));

        var answer = featureService.check(1L, "basic_reports");

        assertThat(answer.allowed()).isFalse();
        assertThat(answer.reason()).isEqualTo("Usage limit is reached");
    }
}
