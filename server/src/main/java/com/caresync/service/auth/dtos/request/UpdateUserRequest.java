package com.caresync.service.auth.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserRequest(
        @NotNull(message = "User ID must be provided for user update")
        String id,

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @Email(message = "Provide a valid email")
        String email,
        String passwordHash
) {}