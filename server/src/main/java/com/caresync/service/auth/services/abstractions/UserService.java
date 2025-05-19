package com.caresync.service.auth.services.abstractions;

import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.response.UserResponse;

public interface UserService {

    void registerUser(RegistrationRequest registrationRequest);
    UserResponse getUserById(Long userId);

}