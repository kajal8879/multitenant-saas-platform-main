package com.kajal.saas.controller;

import com.kajal.saas.dto.OrgDtos.*;
import com.kajal.saas.entity.UserAccount;
import com.kajal.saas.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public OrgResponse getOne(@PathVariable Long id) {
        return organizationService.getOrg(id);
    }

    @PostMapping("/{id}/users")
    public Map<String, Object> addUser(@PathVariable Long id, @Valid @RequestBody InviteUserRequest request) {
        UserAccount user = organizationService.addUser(id, request);
        return Map.of("id", user.getId(), "email", user.getEmail(), "role", user.getRole());
    }
}
