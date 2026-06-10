package com.kajal.saas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.EMAIL;

    @Column(nullable = false)
    private String role = "MEMBER";

    private Instant createdAt = Instant.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
