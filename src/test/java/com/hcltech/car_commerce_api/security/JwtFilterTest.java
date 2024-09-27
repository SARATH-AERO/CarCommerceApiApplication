package com.hcltech.car_commerce_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil; // Mocked JwtUtil

    @InjectMocks
    private JwtFilter jwtFilter; // JwtFilter will use the mocked JwtUtil

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }



    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "invalid.jwt.token";

        // Setup the JwtUtil mock
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn("testUser");
        when(jwtUtil.validateToken(token, "testUser")).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Setup the JwtUtil mock
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void testDoFilterInternal_NullRequest() {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            jwtFilter.doFilterInternal(null, response, filterChain);
        });
    }

    @Test
    void testDoFilterInternal_NullResponse() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            jwtFilter.doFilterInternal(request, null, filterChain);
        });
    }

    @Test
    void testDoFilterInternal_NullFilterChain() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            jwtFilter.doFilterInternal(request, response, null);
        });
    }
}
