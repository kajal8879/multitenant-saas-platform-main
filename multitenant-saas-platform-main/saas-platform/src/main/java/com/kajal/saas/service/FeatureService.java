package com.kajal.saas.service;

import com.kajal.saas.dto.FeatureDtos.FeatureAccessResponse;
import com.kajal.saas.entity.*;
import com.kajal.saas.repo.FeatureUsageRepository;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final SubscriptionRepository subscriptionRepository;
    private final FeatureUsageRepository usageRepository;
    private final OrganizationRepository organizationRepository;

    private final Map<PlanType, Set<String>> features = Map.of(
            PlanType.FREE, Set.of("basic_reports"),
            PlanType.PRO, Set.of("basic_reports", "advanced_reports", "team_export"),
            PlanType.BUSINESS, Set.of("basic_reports", "advanced_reports", "team_export", "audit_logs")
    );

    private final Map<PlanType, Integer> limits = Map.of(
            PlanType.FREE, 10,
            PlanType.PRO, 100,
            PlanType.BUSINESS, 1000
    );

    public FeatureAccessResponse check(Long orgId, String featureKey) {
        Subscription sub = subscriptionRepository.findByOrganizationId(orgId)
                .orElseThrow(() -> new IllegalArgumentException("No subscription found"));
        FeatureUsage usage = usageRepository.findByOrganizationIdAndFeatureKey(orgId, featureKey).orElse(null);
        int used = usage == null ? 0 : usage.getUsedCount();
        int limit = limits.get(sub.getPlan());

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            return new FeatureAccessResponse(false, "Subscription is not active", used, limit);
        }
        if (!features.get(sub.getPlan()).contains(featureKey)) {
            return new FeatureAccessResponse(false, "Feature is not in plan", used, limit);
        }
        if (used >= limit) {
            return new FeatureAccessResponse(false, "Usage limit is reached", used, limit);
        }
        return new FeatureAccessResponse(true, "Allowed", used, limit);
    }

    @Transactional
    public FeatureAccessResponse useFeature(Long orgId, String featureKey) {
        FeatureAccessResponse check = check(orgId, featureKey);
        if (!check.allowed()) {
            return check;
        }
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        FeatureUsage usage = usageRepository.findByOrganizationIdAndFeatureKey(orgId, featureKey)
                .orElseGet(() -> {
                    FeatureUsage u = new FeatureUsage();
                    u.setOrganization(org);
                    u.setFeatureKey(featureKey);
                    return u;
                });
        usage.setUsedCount(usage.getUsedCount() + 1);
        usageRepository.save(usage);
        return new FeatureAccessResponse(true, "Used feature", usage.getUsedCount(), check.limit());
    }
}
