package com.caresync.service.auth.dtos.response;

import com.caresync.service.auth.dtos.request.LocationRequest;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        LocationRequest fullAdress
) {}

// First comment from shadman tabib