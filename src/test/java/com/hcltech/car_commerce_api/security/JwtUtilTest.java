package com.hcltech.car_commerce_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secretKey = "my-very-secure-secret-key-for-jwt";
    @BeforeEach
    void setUp() {
         jwtUtil = new JwtUtil();
        jwtUtil.secretKeyString = secretKey; // Injecting secret key manually
    }

    @Test
    void testGenerateToken() {
        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testUser", "password", roles);

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String username = "testUser";
        String token = createTestToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String username = "testUser";
        String token = createTestToken(username);

        boolean isValid = jwtUtil.validateToken(token, username);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String username = "testUser";
        String token = createTestToken(username);

        boolean isValid = jwtUtil.validateToken(token, "wrongUser");

        assertFalse(isValid);
    }

    @Test
    void testExtractRolesFromToken() {
        List<SimpleGrantedAuthority> role = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testUser", "password", role);

        String token = jwtUtil.generateToken(userDetails);
        List<String> roles = jwtUtil.extractRolesFromToken(token);

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
           }

    @Test
    void testIsTokenExpired_NotExpired() {
        String token = createTestToken("testUser");

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }



    private String createTestToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}