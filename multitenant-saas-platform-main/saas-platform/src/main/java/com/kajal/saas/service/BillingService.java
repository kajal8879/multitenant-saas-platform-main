package com.kajal.saas.service;

import com.kajal.saas.dto.BillingDtos.*;
import com.kajal.saas.entity.Organization;
import com.kajal.saas.entity.Subscription;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.SubscriptionRepository;
import com.stripe.Stripe;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Value("${stripe.secret-key:}")
    private String stripeSecretKey;

    public CheckoutResponse createCheckout(CheckoutRequest request) {
        Stripe.apiKey = stripeSecretKey;
        Organization org = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        // For a real app this would call Stripe Checkout Session.create().
        String fakeUrl = "https://checkout.stripe.com/pay/fake_" + org.getId() + "_" + request.plan();
        return new CheckoutResponse(fakeUrl, "Stripe SDK configured, fake checkout returned for assessment");
    }

    @Transactional
    public SubscriptionResponse applyWebhook(WebhookRequest request) {
        Organization org = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        Subscription sub = subscriptionRepository.findByOrganizationId(org.getId()).orElseGet(() -> {
            Subscription s = new Subscription();
            s.setOrganization(org);
            return s;
        });
        sub.setPlan(request.plan());
        sub.setStatus(request.status());
        sub.setStripeSubscriptionId(request.stripeSubscriptionId());
        subscriptionRepository.save(sub);
        return new SubscriptionResponse(org.getId(), sub.getPlan(), sub.getStatus(), sub.getStripeSubscriptionId());
    }

    public SubscriptionResponse getSubscription(Long orgId) {
        Subscription sub = subscriptionRepository.findByOrganizationId(orgId)
                .orElseThrow(() -> new IllegalArgumentException("No subscription found"));
        return new SubscriptionResponse(orgId, sub.getPlan(), sub.getStatus(), sub.getStripeSubscriptionId());
    }
}
