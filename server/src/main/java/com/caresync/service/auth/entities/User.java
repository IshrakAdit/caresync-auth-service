package com.caresync.service.auth.entities;

import com.caresync.service.auth.dtos.data.Location;
import com.caresync.service.auth.enums.ROLE;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Provide a valid email")
    private String email;

    @Column(name = "password_hash", nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role cannot be null")
    private ROLE role;

    @Column(name = "location_id", unique = true)
    private Long locationId;

    @Transient
    private Location location;

    public User(String userId, String name, String email, String passwordHash, ROLE role) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String userId, String name, String email, String passwordHash, ROLE role, Long locationId) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.locationId = locationId;
    }

}
