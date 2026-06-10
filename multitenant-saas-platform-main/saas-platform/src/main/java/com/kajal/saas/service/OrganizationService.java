package com.kajal.saas.service;

import com.kajal.saas.dto.OrgDtos.*;
import com.kajal.saas.entity.AuthProvider;
import com.kajal.saas.entity.Organization;
import com.kajal.saas.entity.UserAccount;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public OrgResponse getOrg(Long id) {
        Organization org = getOrgEntity(id);
        return new OrgResponse(org.getId(), org.getName(), org.getStripeCustomerId());
    }

    public UserAccount addUser(Long orgId, InviteUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already used");
        }
        UserAccount user = new UserAccount();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setProvider(AuthProvider.EMAIL);
        user.setRole("MEMBER");
        user.setOrganization(getOrgEntity(orgId));
        return userRepository.save(user);
    }

    public Organization getOrgEntity(Long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Organization not found"));
    }
}
