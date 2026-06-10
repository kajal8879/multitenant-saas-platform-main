package com.kajal.saas.service;

import com.kajal.saas.dto.AuthDtos.RegisterRequest;
import com.kajal.saas.repo.OrganizationRepository;
import com.kajal.saas.repo.SubscriptionRepository;
import com.kajal.saas.repo.UserRepository;
import com.kajal.saas.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Test
    void registerCreatesAdminUserAndOrganization() {
        UserRepository userRepository = mock(UserRepository.class);
        OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
        SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
        JwtService jwtService = mock(JwtService.class);

        when(userRepository.existsByEmail("a@test.com")).thenReturn(false);
        when(organizationRepository.save(any())).thenAnswer(inv -> {
            var org = (com.kajal.saas.entity.Organization) inv.getArgument(0);
            org.setId(1L);
            return org;
        });
        when(userRepository.save(any())).thenAnswer(inv -> {
            var user = (com.kajal.saas.entity.UserAccount) inv.getArgument(0);
            user.setId(2L);
            return user;
        });
        when(jwtService.createToken(any())).thenReturn("token");

        AuthService service = new AuthService(userRepository, organizationRepository, subscriptionRepository,
                new BCryptPasswordEncoder(), jwtService);

        var result = service.register(new RegisterRequest("a@test.com", "password", "A", "Test Org"));

        assertThat(result.token()).isEqualTo("token");
        assertThat(result.organizationId()).isEqualTo(1L);
        verify(subscriptionRepository).save(any());
        verify(userRepository).save(argThat(user -> user.getRole().equals("ADMIN")));
    }
}
