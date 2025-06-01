package com.caresync.service.auth.dtos.data;

import com.caresync.service.auth.enums.LOCATION_TYPE;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    private String id;

    private LOCATION_TYPE locationType;

    private String address;
    private String thana;
    private String po;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;

    private String zoneId;
}
