package com.caresync.service.auth.dtos.request;

import jakarta.validation.constraints.*;

public record AddressRequest(

        @NotBlank(message = "Address cannot be blank")
        @Size(min = 2, max = 100, message = "Address must be between 2 and 100 characters")
        String address,

        String thana,

        @NotBlank(message = "PO cannot be blank")
        String po,

        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "Postal code cannot be blank")
        String postalCode
) {}
