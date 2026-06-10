package com.kajal.saas.dto;

import com.kajal.saas.entity.PlanType;
import com.kajal.saas.entity.SubscriptionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BillingDtos {
    public record CheckoutRequest(@NotNull Long organizationId, @NotNull PlanType plan) {}
    public record CheckoutResponse(String checkoutUrl, String fakeMessage) {}
    public record WebhookRequest(@NotBlank String stripeSubscriptionId, @NotNull Long organizationId,
                                 @NotNull PlanType plan, @NotNull SubscriptionStatus status) {}
    public record SubscriptionResponse(Long organizationId, PlanType plan, SubscriptionStatus status,
                                       String stripeSubscriptionId) {}
}
