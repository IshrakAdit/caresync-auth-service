package com.caresync.service.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_locations")
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne(mappedBy = "location")
    private User user;

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 2, max = 100, message = "Address must be between 2 and 100 characters")
    private String address;

    private String thana;

    @NotBlank(message = "PO cannot be blank")
    private String po;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;

    public UserLocation(User user, String address, String thana, String po, String city, String postalCode) {
        this.user = user;
        this.address = address;
        this.thana = thana;
        this.po = po;
        this.city = city;
        this.postalCode = postalCode;
    }
}

