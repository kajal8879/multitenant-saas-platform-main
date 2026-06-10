package com.kajal.saas.controller;

import com.kajal.saas.dto.BillingDtos.*;
import com.kajal.saas.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billing")
public class BillingController {
    private final BillingService billingService;

    @PostMapping("/checkout")
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return billingService.createCheckout(request);
    }

    @PostMapping("/webhook")
    public SubscriptionResponse webhook(@Valid @RequestBody WebhookRequest request) {
        return billingService.applyWebhook(request);
    }

    @GetMapping("/organizations/{orgId}/subscription")
    public SubscriptionResponse subscription(@PathVariable Long orgId) {
        return billingService.getSubscription(orgId);
    }
}
