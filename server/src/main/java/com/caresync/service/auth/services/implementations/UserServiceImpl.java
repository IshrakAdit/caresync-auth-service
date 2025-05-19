package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.entities.UserLocation;
import com.caresync.service.auth.repositories.UserLocationRepository;
import com.caresync.service.auth.repositories.UserRepository;
import com.caresync.service.auth.services.abstractions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserLocationRepository userLocationRepository;

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        if(registrationRequest.fullAddress() == null) {
            userRepository.save(new User(registrationRequest.name(), registrationRequest.email(), registrationRequest.password()));
        }
        else {
            User newUser = User.builder()
                    .name(registrationRequest.name())
                    .email(registrationRequest.email())
                    .passwordHash(registrationRequest.password())
                    .build();

            UserLocation newUserLocation = new UserLocation(
                    newUser, registrationRequest.fullAddress().address(),
                    registrationRequest.fullAddress().thana(),
                    registrationRequest.fullAddress().po(),
                    registrationRequest.fullAddress().city(),
                    registrationRequest.fullAddress().postalCode());

            newUser.setLocation(newUserLocation);

            userLocationRepository.save(newUserLocation);
            userRepository.save(newUser);
        }
    }
}