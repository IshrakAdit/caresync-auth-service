package com.caresync.service.auth.services.abstractions;

import com.caresync.service.auth.dtos.request.LoginRequest;
import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.request.UpdateUserRequest;
import com.caresync.service.auth.dtos.response.UserResponse;

public interface UserService {

    UserResponse getUserById(String userId);
    UserResponse loginUser(LoginRequest loginRequest);
    UserResponse registerUser(RegistrationRequest registrationRequest);
    UserResponse updateUser(UpdateUserRequest updateUserRequest);
    void deleteUser(String userId);

}