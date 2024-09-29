package com.hcltech.car_commerce_api.exception;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;


import static org.junit.jupiter.api.Assertions.assertEquals;


class ResponseExceptionHandlerTest {

    @InjectMocks
    private ResponseExceptionHandler responseExceptionHandler;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAuthenticationException(){
         ProblemDetail problemDetail = responseExceptionHandler.handleAuthenticationException();
        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.getStatus());
        assertEquals("Authentication Error", problemDetail.getDetail());
    }
    @Test
    void testAccessDeniedException(){
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");
        ProblemDetail problemDetail = responseExceptionHandler.handleAccessDeniedException(accessDeniedException);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.getStatus());
        assertEquals("Access Denied", problemDetail.getDetail());
    }

    @Test
    void testJwtException(){
        JwtException jwtException = new JwtException("Jwt Token");
        ProblemDetail problemDetail = responseExceptionHandler.handleJwtTokenException(jwtException);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.getStatus());
        assertEquals("Jwt Token", problemDetail.getDetail());
    }





}
