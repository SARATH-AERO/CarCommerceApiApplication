package com.hcltech.car_commerce_api.exception;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("Resource not found");
        ProblemDetail response = globalExceptionHandler.notFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Resource not found not Found.", response.getDetail());
    }

    @Test
    void testAlreadyExistException() {
        AlreadyExistException exception = new AlreadyExistException("Resource already exists");
        ProblemDetail response = globalExceptionHandler.alreadyExistException(exception);
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertEquals("Resource already exists already Exist.", response.getDetail());
    }

    @Test
    void testHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ProblemDetail response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("fieldName-error message/", response.getDetail());
    }

    @Test
    void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        ProblemDetail response = globalExceptionHandler.handleBadCredentialsException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("Invalid credentials", response.getDetail());
    }

}