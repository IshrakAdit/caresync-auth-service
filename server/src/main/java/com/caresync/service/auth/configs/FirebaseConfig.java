package com.caresync.service.auth.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIAL}")
    private String firebaseCredentialsBase64;

    @Bean
    @Profile("!e2e-test") // Skip Firebase initialization during E2E tests
    public FirebaseApp initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // Decode Base64 string to obtain the JSON content
            byte[] decodedBytes = Base64.getDecoder().decode(firebaseCredentialsBase64);
            String credentialsJson = new String(decodedBytes, StandardCharsets.UTF_8);

            // Create credentials instance from the JSON
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedBytes);
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

            // Initialize Firebase with the credentials
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    // No FirebaseApp bean for E2E tests - let it be null
    // The FirebaseJwtDecoder will handle null gracefully
}