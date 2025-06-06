package com.caresync.service.auth.dtos.request;

import com.caresync.service.auth.dtos.data.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(

        @NotNull(message = "User ID cannot be blank")
        String userId,

        @NotNull(message = "Requires access token for registration")
        String accessToken,

        @NotNull(message = "Name cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters long.")
        String name,

        @NotNull(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Password cannot be blank")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
                message = "Password must include at least one number and one special character."
        )
        String password,

        @Valid
        Location location
) {}
