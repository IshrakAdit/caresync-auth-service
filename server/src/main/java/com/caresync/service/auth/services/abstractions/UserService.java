package com.caresync.service.auth.services.abstractions;

import com.caresync.service.auth.dtos.request.RegistrationRequest;

public interface UserService {

    void registerUser(RegistrationRequest registrationRequest);

}