package com.caresync.service.auth.dtos.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OtpVerificationRequest {
    private String email;
    private String otp;
}