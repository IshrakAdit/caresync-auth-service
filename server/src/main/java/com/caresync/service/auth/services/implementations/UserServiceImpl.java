package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

//    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        userRepository.save(new User(registrationRequest.email(), registrationRequest.fullName(), registrationRequest.password(), registrationRequest.phoneNumber()));
    }
}