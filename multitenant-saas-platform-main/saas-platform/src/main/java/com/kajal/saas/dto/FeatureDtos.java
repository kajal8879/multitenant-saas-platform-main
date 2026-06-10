package com.kajal.saas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeatureDtos {
    public record FeatureCheckRequest(@NotNull Long organizationId, @NotBlank String featureKey) {}
    public record FeatureUseRequest(@NotNull Long organizationId, @NotBlank String featureKey) {}
    public record FeatureAccessResponse(boolean allowed, String reason, int used, int limit) {}
}
