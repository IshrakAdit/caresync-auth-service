package com.caresync.service.auth.dtos.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        LocationResponse locationResponse
) {}

// First comment from shadman tabib