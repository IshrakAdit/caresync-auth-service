package com.caresync.service.auth.configs;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirebaseJwtDecoder implements JwtDecoder {
    private final FirebaseAuth firebaseAuth;

    public FirebaseJwtDecoder(@Autowired(required = false) FirebaseApp firebaseApp) {
        // Handle null FirebaseApp during E2E tests
        this.firebaseAuth = firebaseApp != null ? FirebaseAuth.getInstance(firebaseApp) : null;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        // For E2E tests, create a mock JWT without Firebase validation
        if (firebaseAuth == null) {
            return createMockJwt(token);
        }
        
        try {
            FirebaseToken firebaseToken = validateToken(token);
            return createJwt(firebaseToken, token);
        } catch (FirebaseAuthException e) {
            AuthErrorCode authErrorCode = e.getAuthErrorCode();
            throw new JwtValidationException(e.getMessage(),
                    List.of(new OAuth2Error(authErrorCode.name(), e.getMessage(), null)));
        }
    }

    private Jwt createJwt(FirebaseToken firebaseToken, String token) {
        return Jwt.withTokenValue(token)
                .header("alg", "RS256")
                .header("type", "JWT")
                .subject(firebaseToken.getUid())
                .claim("email", firebaseToken.getEmail())
                .claim("email_verified", firebaseToken.isEmailVerified())
                .claim("iss", firebaseToken.getIssuer())
                .claim("scp", firebaseToken.getClaims().get("scp"))
                .build();
    }

    private FirebaseToken validateToken(String token) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(token);
    }

    private Jwt createMockJwt(String token) {
        // Create a mock JWT for E2E tests
        return Jwt.withTokenValue(token)
                .header("alg", "none")
                .header("type", "JWT")
                .subject("e2e-test-user")
                .claim("email", "e2e-test@example.com")
                .claim("email_verified", true)
                .claim("iss", "e2e-test-issuer")
                .claim("scp", "test")
                .build();
    }
}
