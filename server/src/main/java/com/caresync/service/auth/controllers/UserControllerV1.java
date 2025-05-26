package com.caresync.service.auth.controllers;

import com.caresync.service.auth.dtos.request.LoginRequest;
import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.response.UserResponse;
import com.caresync.service.auth.services.abstractions.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class UserControllerV1 {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User service test successful");
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok("User login test successful");
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest){
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok().build();
    }
}
