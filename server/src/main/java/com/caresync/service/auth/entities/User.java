package com.caresync.service.auth.entities;

import com.caresync.service.auth.dtos.data.Location;
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

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "location_id", unique = true)
    private Long locationId;

    @Transient
    private Location location;

    public User(String userId, String name, String email, String passwordHash) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User(String userId, String name, String email, String passwordHash, Long locationId) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.locationId = locationId;
    }

}
