package com.caresync.service.auth.dtos.response;

import com.caresync.service.auth.enums.ROLE;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        ROLE role,
        LocationResponse locationResponse
) {}
