// package com.caresync.service.auth.e2e;

// import com.caresync.service.auth.dtos.request.LoginRequest;
// import com.caresync.service.auth.dtos.request.RegistrationRequest;
// import com.caresync.service.auth.dtos.response.UserResponse;
// import com.caresync.service.auth.dtos.data.Location;
// import com.caresync.service.auth.enums.LOCATION_TYPE;
// import com.caresync.service.auth.repositories.UserRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import static org.hamcrest.Matchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureMockMvc
// @Testcontainers
// @ActiveProfiles("e2e-test")  // Separate profile for E2E tests
// @Transactional  // Automatic rollback after each test
// class UserLoginWorkflowE2ETest {

//     // 🐳 Separate test database container - NEVER touches your real database
//     @Container
//     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
//             .withDatabaseName("caresync_e2e_test")
//             .withUsername("test_user")
//             .withPassword("test_password")
//             .withReuse(false);  // Fresh container for each test run

//     @DynamicPropertySource
//     static void configureProperties(DynamicPropertyRegistry registry) {
//         // Override database connection for E2E tests only
//         registry.add("spring.datasource.url", postgres::getJdbcUrl);
//         registry.add("spring.datasource.username", postgres::getUsername);
//         registry.add("spring.datasource.password", postgres::getPassword);
        
//         // Force PostgreSQL driver to avoid H2 conflicts
//         registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
//         registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//         registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        
//         // Disable Flyway for E2E tests
//         registry.add("spring.flyway.enabled", () -> "false");
        
//         // Provide dummy Firebase credentials for E2E tests
//         registry.add("FIREBASE_CREDENTIAL", () -> "eyJ0eXBlIjoic2VydmljZV9hY2NvdW50IiwicHJvamVjdF9pZCI6ImU2ZS10ZXN0LXByb2plY3QiLCJwcml2YXRlX2tleV9pZCI6ImR1bW15LWtleS1pZCIsInByaXZhdGVfa2V5IjoiLS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tXG5kdW1teS1wcml2YXRlLWtleVxuLS0tLS1FTkQgUFJJVkFURSBLRVktLS0tLVxuIiwiY2xpZW50X2VtYWlsIjoiZTJlLXRlc3RAZTJlLXRlc3QtcHJvamVjdC5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsImNsaWVudF9pZCI6IjEyMzQ1Njc4OTAiLCJhdXRoX3VyaSI6Imh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9vL29hdXRoMi9hdXRoIiwidG9rZW5fdXJpIjoiaHR0cHM6Ly9vYXV0aDIuZ29vZ2xlYXBpcy5jb20vdG9rZW4ifQ==");
//     }

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Autowired
//     private UserRepository userRepository;

//     private Location testLocation;
//     private RegistrationRequest registrationRequest;

//     @BeforeEach
//     void setUp() {
//         // Fresh test data for each test
//         testLocation = Location.builder()
//                 .locationType(LOCATION_TYPE.USER)
//                 .address("123 E2E Test Street")
//                 .thana("Test Thana")
//                 .po("Test PO")
//                 .city("Test City")
//                 .postalCode(1234L)
//                 .zoneId(1L)
//                 .build();

//         registrationRequest = new RegistrationRequest(
//                 "e2e-user-001",
//                 "firebase-access-token-e2e",
//                 "E2E Test User",
//                 "e2etest@example.com",
//                 "TestPassword123!",  // Valid password with number and special character
//                 testLocation
//         );
//     }

//     @Test
//     @DisplayName("E2E Test 1: Complete User Registration and Login Workflow")
//     void completeUserRegistrationAndLoginWorkflow_ShouldWorkEndToEnd() throws Exception {
//         // 🎯 STEP 1: Register a new user (E2E)
//         String registrationJson = objectMapper.writeValueAsString(registrationRequest);

//         mockMvc.perform(post("/user/v1/register")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(registrationJson))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id", is("e2e-user-001")))
//                 .andExpect(jsonPath("$.name", is("E2E Test User")))
//                 .andExpect(jsonPath("$.email", is("e2etest@example.com")))
//                 .andExpect(jsonPath("$.locationResponse.city", is("Test City")));

//         // 🔍 Verify user was actually saved to database
//         var savedUser = userRepository.findById("e2e-user-001");
//         assert savedUser.isPresent();
//         assert savedUser.get().getName().equals("E2E Test User");

//         // 🎯 STEP 2: Login with the registered user (E2E)
//         LoginRequest loginRequest = new LoginRequest("e2e-user-001", "firebase-access-token-e2e");
//         String loginJson = objectMapper.writeValueAsString(loginRequest);

//         mockMvc.perform(post("/user/v1/login")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(loginJson))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id", is("e2e-user-001")))
//                 .andExpect(jsonPath("$.name", is("E2E Test User")))
//                 .andExpect(jsonPath("$.email", is("e2etest@example.com")))
//                 .andExpect(jsonPath("$.locationResponse.address", is("123 E2E Test Street")));

//         // 🎯 STEP 3: Access user profile (authenticated request)
//         mockMvc.perform(get("/user/v1/get/e2e-user-001"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name", is("E2E Test User")))
//                 .andExpect(jsonPath("$.locationResponse.city", is("Test City")));

//         System.out.println("✅ E2E Test 1 PASSED: Complete registration → login → profile access workflow");
//     }

//     @Test
//     @DisplayName("E2E Test 2: Login with Invalid Credentials - Error Handling")
//     void loginWithInvalidCredentials_ShouldHandleErrorsCorrectly() throws Exception {
//         // 🎯 STEP 1: Try to login with non-existent user
//         LoginRequest invalidLoginRequest = new LoginRequest("non-existent-user", "invalid-token");
//         String invalidLoginJson = objectMapper.writeValueAsString(invalidLoginRequest);

//         mockMvc.perform(post("/user/v1/login")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(invalidLoginJson))
//                 .andExpect(status().isBadRequest())  // 400 BAD_REQUEST is what the error handler returns
//                 .andExpect(content().string(containsString("No user found with ID: non-existent-user")));

//         // 🎯 STEP 2: Try to access protected resource without authentication
//         mockMvc.perform(get("/user/v1/get/non-existent-user"))
//                 .andExpect(status().isNotFound());

//         // 🎯 STEP 3: Try to register user with duplicate ID
//         String duplicateRegistrationJson = objectMapper.writeValueAsString(registrationRequest);

//         // First registration should succeed
//         mockMvc.perform(post("/user/v1/register")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(duplicateRegistrationJson))
//                 .andExpect(status().isCreated());

//         // Second registration with same ID should fail
//         mockMvc.perform(post("/user/v1/register")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(duplicateRegistrationJson))
//                 .andExpect(status().isBadRequest())  // Check what the actual response is
//                 .andExpect(content().string(containsString("User already exists with ID: e2e-user-001")));

//         System.out.println("✅ E2E Test 2 PASSED: Invalid credentials and duplicate registration error handling");
//     }
// } 