package com.asusoftware.SmartMag_Api.user.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private UUID keycloakId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;
}
