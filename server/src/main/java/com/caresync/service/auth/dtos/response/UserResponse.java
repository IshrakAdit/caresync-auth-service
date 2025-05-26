package com.caresync.service.auth.dtos.response;

import com.caresync.service.auth.dtos.request.AddressRequest;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        AddressRequest fullAdress
) {}

// First comment from shadman tabib