package com.kajal.saas.controller;

import com.kajal.saas.dto.FeatureDtos.*;
import com.kajal.saas.service.FeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/features")
public class FeatureController {
    private final FeatureService featureService;

    @PostMapping("/check")
    public FeatureAccessResponse check(@Valid @RequestBody FeatureCheckRequest request) {
        return featureService.check(request.organizationId(), request.featureKey());
    }

    @PostMapping("/use")
    public FeatureAccessResponse use(@Valid @RequestBody FeatureUseRequest request) {
        return featureService.useFeature(request.organizationId(), request.featureKey());
    }
}
