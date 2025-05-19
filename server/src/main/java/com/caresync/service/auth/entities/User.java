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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Provide a valid email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String passwordHash;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 255, message = "Location must be at most 255 characters")
    private UserLocation location;

    public User(String name, String email, String passwordHash) {
        this(name, email, passwordHash, null);
    }

    public User(String name, String email, String passwordHash, UserLocation location) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.location = location;
    }
}
