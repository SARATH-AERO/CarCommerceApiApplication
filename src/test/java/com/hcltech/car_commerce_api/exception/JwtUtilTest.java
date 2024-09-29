package com.hcltech.car_commerce_api.exception;
import com.hcltech.car_commerce_api.security.JwtUtil;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String secretKeyString = "mysecretkeyforjwttokenmysecretkeyforjwttoken";
        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        jwtUtil.secretKey = secretKey;
    }

    @Test
    void testGenerateToken() {
        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testUser", "password", roles);

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        String username = jwtUtil.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        boolean isValid = jwtUtil.validateToken(token, "testUser");

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        boolean isValid = jwtUtil.validateToken(token, "invalidUser");

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testExtractRolesFromToken() {
        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testUser", "password", roles);

        String token = jwtUtil.generateToken(userDetails);

        List<String> extractedRoles = jwtUtil.extractRolesFromToken(token);
        assertTrue(extractedRoles.contains("ROLE_USER"));
    }
}
