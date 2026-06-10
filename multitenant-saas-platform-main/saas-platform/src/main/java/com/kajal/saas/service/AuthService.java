package com.kajal.saas.service;

import com.kajal.saas.dto.AuthDtos.*;
import com.kajal.saas.entity.*;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.SubscriptionRepository;
import com.kajal.saas.repo.UserRepository;
import com.kajal.saas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already used");
        }

        Organization org = new Organization();
        org.setName(request.organizationName());
        organizationRepository.save(org);

        Subscription sub = new Subscription();
        sub.setOrganization(org);
        sub.setPlan(PlanType.FREE);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(sub);

        UserAccount user = new UserAccount();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole("ADMIN");
        user.setProvider(AuthProvider.EMAIL);
        user.setOrganization(org);
        userRepository.save(user);

        return new AuthResponse(jwtService.createToken(user), user.getId(), org.getId());
    }

    public AuthResponse login(LoginRequest request) {
        UserAccount user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Bad email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Bad email or password");
        }
        return new AuthResponse(jwtService.createToken(user), user.getId(), user.getOrganization().getId());
    }
}
