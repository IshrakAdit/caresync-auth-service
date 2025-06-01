package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.response.UserResponse;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.repositories.UserRepository;
import com.caresync.service.auth.services.abstractions.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .fullAdress(null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void registerUser(RegistrationRequest registrationRequest) {
        // Check if user already exists
        if (userRepository.existsById(registrationRequest.userId())) {
            throw new DataIntegrityViolationException("User already exists with ID: " + registrationRequest.userId());
        }

        try {
            User newUser = User.builder()
                    .id(registrationRequest.userId())
                    .name(registrationRequest.name())
                    .email(registrationRequest.email())
                    .passwordHash(registrationRequest.password())
                    .build();

            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsById(registrationRequest.userId())) {
                userRepository.deleteById(registrationRequest.userId());
            }
            throw new DataIntegrityViolationException("Failed to register user: " + e.getMessage());
        }
    }
}