package com.caresync.service.auth.services.implementations;

import com.caresync.service.auth.dtos.request.AddressRequest;
import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.response.UserResponse;
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

    private UserResponse mapToResponse(User user) {
        UserLocation location = user.getLocation();
        AddressRequest address = null;
        if (location != null) {
            address = new AddressRequest(
                    location.getAddress(),
                    location.getThana(),
                    location.getPo(),
                    location.getCity(),
                    location.getPostalCode()
            );
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .fullAdress(address)
                .build();
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return mapToResponse(user);
    }

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        if(registrationRequest.fullAddress() == null) {
            userRepository.save(new User(registrationRequest.userId(), registrationRequest.name(), registrationRequest.email(), registrationRequest.password()));
        }
        else {
            User newUser = User.builder()
                    .id(registrationRequest.userId())
                    .name(registrationRequest.name())
                    .email(registrationRequest.email())
                    .passwordHash(registrationRequest.password())
                    .build();

            UserLocation newUserLocation = UserLocation.builder().
                    address(registrationRequest.fullAddress().address()).
                    thana(registrationRequest.fullAddress().thana()).
                    po(registrationRequest.fullAddress().po()).
                    city(registrationRequest.fullAddress().city()).
                    postalCode(registrationRequest.fullAddress().postalCode()).
                    user(newUser)
                    .build();

            newUser.setLocation(newUserLocation);

            userRepository.save(newUser);
        }
    }
}