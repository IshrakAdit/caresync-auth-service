package com.caresync.service.auth.dtos.response;

import com.caresync.service.auth.enums.LOCATION_TYPE;
import lombok.Builder;

@Builder
public record LocationResponse(
        Long id,
        LOCATION_TYPE locationType,
        String address,
        String thana,
        String po,
        String city,
        Long postalCode,
        Long zoneId
) {}
