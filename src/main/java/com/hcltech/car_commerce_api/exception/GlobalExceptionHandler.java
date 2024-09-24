package com.hcltech.car_commerce_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail notFoundException(NotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,String.format("Not Found %s",
                        exception.getMessage()));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ProblemDetail alreadyExistException(NotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,String.format("Already exist %s",
                        exception.getMessage()));
    }

//    handle invalid input errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, error ->
                        Optional.ofNullable(error.getDefaultMessage()).orElse("Unknown error")));


        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,WebRequest request) {
        Map<String, Object> resonseBody = new HashMap<>();
        resonseBody.put("timestamp", LocalDateTime.now());
        resonseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        resonseBody.put("error", "Conflict");
        resonseBody.put("message", ex.getMessage());
        resonseBody.put("path", request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resonseBody);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDeniedException() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,"Access Denied: You do not have the " +
                        "necessary permissions to access this resource.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGlobalException() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred.");

    }
}

