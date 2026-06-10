package com.kajal.saas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String stripeCustomerId;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "organization")
    private List<UserAccount> users = new ArrayList<>();

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    private Subscription subscription;
}
