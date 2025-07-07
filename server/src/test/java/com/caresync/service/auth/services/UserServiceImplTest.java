package com.caresync.service.auth.services;

import com.caresync.service.auth.clients.LocationClient;
import com.caresync.service.auth.dtos.data.Location;
import com.caresync.service.auth.dtos.request.LocationRequest;
import com.caresync.service.auth.dtos.request.LoginRequest;
import com.caresync.service.auth.dtos.request.RegistrationRequest;
import com.caresync.service.auth.dtos.request.UpdateUserRequest;
import com.caresync.service.auth.dtos.response.LocationResponse;
import com.caresync.service.auth.dtos.response.UserResponse;
import com.caresync.service.auth.entities.User;
import com.caresync.service.auth.enums.LOCATION_TYPE;
import com.caresync.service.auth.repositories.UserRepository;
import com.caresync.service.auth.services.implementations.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationClient locationClient;

    @InjectMocks
    private UserServiceImpl userService;

    private static User testUser;
    private static LocationResponse testLocationResponse;
    private static Location testLocation;
    private static RegistrationRequest testRegistrationRequest;
    private static LoginRequest testLoginRequest;
    private static UpdateUserRequest testUpdateUserRequest;

    @BeforeAll
    public static void init() {
        System.out.println("BeforeAll - Initializing test data");
        
        // Initialize test user
        testUser = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(1L)
                .build();

        // Initialize test location
        testLocation = Location.builder()
                .id(1L)
                .locationType(LOCATION_TYPE.USER)
                .address("123 Main St")
                .thana("Dhanmondi")
                .po("Dhanmondi")
                .city("Dhaka")
                .postalCode(1205L)
                .zoneId(1L)
                .build();

        // Initialize test location response
        testLocationResponse = LocationResponse.builder()
                .id(1L)
                .locationType(LOCATION_TYPE.USER)
                .address("123 Main St")
                .thana("Dhanmondi")
                .po("Dhanmondi")
                .city("Dhaka")
                .postalCode(1205L)
                .zoneId(1L)
                .build();

        // Initialize test registration request
        testRegistrationRequest = new RegistrationRequest(
                "user123",
                "access-token-123",
                "John Doe",
                "john.doe@example.com",
                "password123!",
                testLocation
        );

        // Initialize test login request
        testLoginRequest = new LoginRequest(
                "user123",
                "access-token-123"
        );

        // Initialize test update request
        testUpdateUserRequest = UpdateUserRequest.builder()
                .id("user123")
                .name("John Updated")
                .email("john.updated@example.com")
                .passwordHash("newHashedPassword")
                .build();
    }

    @BeforeEach
    public void initEachTest() {
        System.out.println("BeforeEach - Setting up test");
        reset(userRepository, locationClient);
        
        // Recreate testUser for each test to avoid shared state issues
        testUser = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(1L)
                .build();
    }

    // ============ GET USER BY ID TESTS ============

    @Test
    @DisplayName("Should get user by ID successfully")
    void getUserById_ShouldReturnUserSuccessfully() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = userService.getUserById("user123");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
        assertNotNull(result.locationResponse());
        assertEquals(1L, result.locationResponse().id());
        
        verify(userRepository, times(1)).findById("user123");
        verify(locationClient, times(1)).getLocationById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void getUserById_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById("nonexistent");
        });

        assertEquals("User not found with id: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).findById("nonexistent");
        verify(locationClient, never()).getLocationById(any());
    }

    @Test
    @DisplayName("Should get user by ID without location when locationId is null")
    void getUserById_ShouldReturnUserWithoutLocationWhenLocationIdIsNull() {
        // Arrange
        User userWithoutLocation = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(null)
                .build();

        when(userRepository.findById("user123")).thenReturn(Optional.of(userWithoutLocation));

        // Act
        UserResponse result = userService.getUserById("user123");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertNull(result.locationResponse());
        
        verify(userRepository, times(1)).findById("user123");
        verify(locationClient, never()).getLocationById(any());
    }

    // ============ LOGIN USER TESTS ============

    @Test
    @DisplayName("Should login user successfully")
    void loginUser_ShouldLoginUserSuccessfully() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = userService.loginUser(testLoginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, never()).existsById(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user does not exist for login")
    void loginUser_ShouldThrowExceptionWhenUserNotExists() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.loginUser(new LoginRequest("nonexistent", "access-token-123"));
        });

        assertEquals("No user found with ID: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).findById("nonexistent");
        verify(userRepository, never()).existsById(any());
    }

    // ============ REGISTER USER TESTS ============

    @Test
    @DisplayName("Should register user successfully")
    void registerUser_ShouldRegisterUserSuccessfully() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(false);
        when(locationClient.createLocation(any(LocationRequest.class))).thenReturn(testLocationResponse);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse result = userService.registerUser(testRegistrationRequest);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
        assertNotNull(result.locationResponse());
        
        verify(userRepository, times(1)).existsById("user123");
        verify(locationClient, times(1)).createLocation(any(LocationRequest.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when user already exists")
    void registerUser_ShouldThrowExceptionWhenUserAlreadyExists() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userService.registerUser(testRegistrationRequest);
        });

        assertEquals("User already exists with ID: user123", exception.getMessage());
        verify(userRepository, times(1)).existsById("user123");
        verify(locationClient, never()).createLocation(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle registration failure and cleanup user")
    void registerUser_ShouldHandleRegistrationFailureAndCleanup() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(false);
        when(locationClient.createLocation(any(LocationRequest.class))).thenReturn(testLocationResponse);
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Database error"));
        doNothing().when(userRepository).deleteById("user123");

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userService.registerUser(testRegistrationRequest);
        });

        assertTrue(exception.getMessage().contains("Failed to register user"));
        verify(userRepository, times(1)).deleteById("user123");
    }

    // ============ UPDATE USER TESTS ============

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_ShouldUpdateUserSuccessfully() {
        // Arrange
        User updatedUser = User.builder()
                .id("user123")
                .name("John Updated")
                .email("john.updated@example.com")
                .passwordHash("newHashedPassword")
                .locationId(1L)
                .build();

        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = userService.updateUser(testUpdateUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Updated", result.name());
        assertEquals("john.updated@example.com", result.email());
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void updateUser_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id("nonexistent")
                .name("New Name")
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(request);
        });

        assertEquals("User not found with id: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).findById("nonexistent");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update user with partial data")
    void updateUser_ShouldUpdateUserWithPartialData() {
        // Arrange
        UpdateUserRequest partialRequest = UpdateUserRequest.builder()
                .id("user123")
                .name("John Partial Update")
                .build();

        User partiallyUpdatedUser = User.builder()
                .id("user123")
                .name("John Partial Update")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(1L)
                .build();

        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(partiallyUpdatedUser);
        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = userService.updateUser(partialRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Partial Update", result.name());
        assertEquals("john.doe@example.com", result.email()); // Should remain unchanged
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ============ DELETE USER TESTS ============

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_ShouldDeleteUserSuccessfully() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // Act
        userService.deleteUser("user123");

        // Assert
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void deleteUser_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser("nonexistent");
        });

        assertEquals("User not found with id: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).findById("nonexistent");
        verify(userRepository, never()).delete(any());
    }

    // ============ PRIVATE METHOD TESTS ============

    @Test
    @DisplayName("Should test private mapToResponse method with location")
    void testPrivateMethod_mapToResponse_WithLocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Method mapToResponseMethod = UserServiceImpl.class.getDeclaredMethod("mapToResponse", User.class, LocationResponse.class);
        mapToResponseMethod.setAccessible(true);

        // Act
        UserResponse result = (UserResponse) mapToResponseMethod.invoke(userService, testUser, testLocationResponse);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
        assertNotNull(result.locationResponse());
        assertEquals(1L, result.locationResponse().id());
    }

    @Test
    @DisplayName("Should test private mapToResponse method without location")
    void testPrivateMethod_mapToResponse_WithoutLocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Method mapToResponseMethod = UserServiceImpl.class.getDeclaredMethod("mapToResponse", User.class, LocationResponse.class);
        mapToResponseMethod.setAccessible(true);

        User userWithoutLocation = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(null)
                .build();

        // Act
        UserResponse result = (UserResponse) mapToResponseMethod.invoke(userService, userWithoutLocation, null);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
        assertNull(result.locationResponse());
    }

    @Test
    @DisplayName("Should test private mapToResponse method with location lookup")
    void testPrivateMethod_mapToResponse_WithLocationLookup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Method mapToResponseMethod = UserServiceImpl.class.getDeclaredMethod("mapToResponse", User.class, LocationResponse.class);
        mapToResponseMethod.setAccessible(true);

        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = (UserResponse) mapToResponseMethod.invoke(userService, testUser, null);

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.id());
        assertNotNull(result.locationResponse());
        assertEquals(1L, result.locationResponse().id());
        verify(locationClient, times(1)).getLocationById(1L);
    }

    // ============ EDGE CASE TESTS ============

    @Test
    @DisplayName("Should handle null values in update request")
    void updateUser_ShouldHandleNullValues() {
        // Arrange
        UpdateUserRequest nullRequest = UpdateUserRequest.builder()
                .id("user123")
                .name(null)
                .email(null)
                .passwordHash(null)
                .build();

        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(locationClient.getLocationById(1L)).thenReturn(testLocationResponse);

        // Act
        UserResponse result = userService.updateUser(nullRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.name()); // Should remain unchanged
        assertEquals("john.doe@example.com", result.email()); // Should remain unchanged
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle empty strings in registration")
    void registerUser_ShouldHandleEmptyStrings() {
        // Arrange
        RegistrationRequest emptyRequest = new RegistrationRequest(
                "",
                "access-token-123",
                "",
                "",
                "",
                testLocation
        );

        when(userRepository.existsById("")).thenReturn(false);
        when(locationClient.createLocation(any(LocationRequest.class))).thenReturn(testLocationResponse);

        User emptyUser = User.builder()
                .id("")
                .name("")
                .email("")
                .passwordHash("")
                .locationId(1L)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(emptyUser);

        // Act
        UserResponse result = userService.registerUser(emptyRequest);

        // Assert
        assertNotNull(result);
        assertEquals("", result.id());
        assertEquals("", result.name());
        assertEquals("", result.email());
        
        verify(userRepository, times(1)).existsById("");
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ============ EXCEPTIONAL SCENARIOS ============

    @Test
    @DisplayName("Should handle location client failure during registration")
    void registerUser_ShouldHandleLocationClientFailure() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(false);
        when(locationClient.createLocation(any(LocationRequest.class)))
                .thenThrow(new RuntimeException("Location service unavailable"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(testRegistrationRequest);
        });

        assertEquals("Location service unavailable", exception.getMessage());
        verify(userRepository, times(1)).existsById("user123");
        verify(locationClient, times(1)).createLocation(any(LocationRequest.class));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle location client failure during getUserById")
    void getUserById_ShouldHandleLocationClientFailure() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(locationClient.getLocationById(1L)).thenThrow(new RuntimeException("Location service unavailable"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById("user123");
        });

        assertEquals("Location service unavailable", exception.getMessage());
        verify(userRepository, times(1)).findById("user123");
        verify(locationClient, times(1)).getLocationById(1L);
    }

    // ============ MOCKING VOID METHODS ============

    @Test
    @DisplayName("Should verify delete method is called correctly")
    void deleteUser_ShouldVerifyDeleteMethodCall() {
        // Arrange
        User freshUser = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashedPassword123")
                .locationId(1L)
                .build();
        
        when(userRepository.findById("user123")).thenReturn(Optional.of(freshUser));
        doNothing().when(userRepository).delete(freshUser);

        // Act
        userService.deleteUser("user123");

        // Assert - Using argument captor to verify exact object passed
        verify(userRepository, times(1)).delete(argThat(user -> 
            user.getId().equals("user123") && 
            user.getName().equals("John Doe")
        ));
    }

    @AfterEach
    public void cleanup() {
        System.out.println("AfterEach - Cleaning up test");
        verifyNoMoreInteractions(userRepository, locationClient);
    }

    @AfterAll
    public static void destroy() {
        System.out.println("AfterAll - Test suite completed");
    }

    // hola
}