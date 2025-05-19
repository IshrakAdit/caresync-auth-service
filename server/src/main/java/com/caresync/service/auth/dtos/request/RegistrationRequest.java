package com.caresync.service.auth.dtos.request;

import jakarta.validation.constraints.*;

public record RegistrationRequest(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters long.")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
                message = "Password must include at least one number and one special character."
        )
        String password,

        @NotBlank(message = "Address cannot be blank")
        @Size(min = 2, max = 100, message = "Address must be between 2 and 100 characters")
        String address,

        String thana,

        @NotBlank(message = "PO cannot be blank")
        String po,

        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "Postal code cannot be blank")
        String postalCode
) {}
