package com.caresync.service.auth.dtos.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        String address,
        String thana,
        String po,
        String city,
        String postalCode
) {}