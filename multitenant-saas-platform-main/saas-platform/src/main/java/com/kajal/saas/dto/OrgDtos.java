package com.kajal.saas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OrgDtos {
    public record OrgResponse(Long id, String name, String stripeCustomerId) {}
    public record InviteUserRequest(@Email String email, @NotBlank String name) {}
}
