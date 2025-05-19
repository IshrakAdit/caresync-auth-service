package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.repositories.UserRepository;
import com.caresync.service.auth.services.abstractions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        if(registrationRequest.location().isBlank()) {
            userRepository.save(new User(registrationRequest.name(), registrationRequest.email(), registrationRequest.password()));
        }
        else {
            userRepository.save(new User(registrationRequest.name(), registrationRequest.email(), registrationRequest.password()));
        }
    }
}