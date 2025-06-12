package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.clients.LocationClient;
import com.caresync.service.auth.dtos.data.Location;
import com.caresync.service.auth.dtos.request.LocationRequest;
import com.caresync.service.auth.dtos.request.LoginRequest;
import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.request.UpdateUserRequest;
import com.caresync.service.auth.dtos.response.LocationResponse;
import com.caresync.service.auth.dtos.response.UserResponse;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.repositories.UserRepository;
import com.caresync.service.auth.services.abstractions.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LocationClient locationClient;

    private UserResponse mapToResponse(User user, LocationResponse locationResponse) {
        if (locationResponse == null && user.getLocationId() != null) {
            locationResponse = locationClient.getLocationById(user.getLocationId());
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .locationResponse(locationResponse)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToResponse(user, null);
    }

    @Override
    @Transactional
    public UserResponse loginUser(LoginRequest loginRequest) {
        if (!userRepository.existsById(loginRequest.userId())) {
            throw new EntityNotFoundException("No user found with ID: " + loginRequest.userId());
        }

        User user = userRepository.findById(loginRequest.userId()).isPresent() ? userRepository.findById(loginRequest.userId()).get() : null;
        return mapToResponse(user, null);
    }

    @Override
    @Transactional
    public UserResponse registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsById(registrationRequest.userId())) {
            throw new DataIntegrityViolationException("User already exists with ID: " + registrationRequest.userId());
        }

        try {
            Location location = registrationRequest.location();

            LocationRequest locationRequest = LocationRequest.builder()
                    .id(location.getId()) // optional
                    .locationType(location.getLocationType())
                    .address(location.getAddress())       // optional
                    .thana(location.getThana())           // optional
                    .po(location.getPo())                 // optional
                    .city(location.getCity())
                    .postalCode(location.getPostalCode())
                    .zoneId(location.getZoneId())
                    .build();

            LocationResponse locationResponse = locationClient.createLocation(locationRequest);

            User newUser = User.builder()
                    .id(registrationRequest.userId())
                    .name(registrationRequest.name())
                    .email(registrationRequest.email())
                    .passwordHash(registrationRequest.password())
                    .locationId(locationResponse.id())
                    .build();

            userRepository.save(newUser);
            return mapToResponse(newUser, locationResponse);

        } catch (DataIntegrityViolationException e) {
            userRepository.deleteById(registrationRequest.userId());
            throw new DataIntegrityViolationException("Failed to register user: " + e.getMessage());
        }
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(updateUserRequest.id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updateUserRequest.id()));

        if (updateUserRequest.name() != null) {
            user.setName(updateUserRequest.name());
        }
        if (updateUserRequest.email() != null) {
            user.setEmail(updateUserRequest.email());
        }
        if (updateUserRequest.passwordHash() != null) {
            user.setPasswordHash(updateUserRequest.passwordHash());
        }

        return mapToResponse(userRepository.save(user), null);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(user);
    }

}