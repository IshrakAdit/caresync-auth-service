package com.caresync.service.auth.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "User ID cannot be blank")
        String userId,

        @NotBlank(message = "Requires access token for registration")
        String accessToken

) {}