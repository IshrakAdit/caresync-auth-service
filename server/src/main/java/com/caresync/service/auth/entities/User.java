package com.caresync.service.auth.entities;

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

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String passwordHash;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public User(String userId, String name, String email, String passwordHash) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.location = null;
    }
}
