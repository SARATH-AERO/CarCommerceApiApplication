package com.hcltech.car_commerce_api.exception;

import org.springframework.http.HttpStatus;
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

    // Handle BuyerNotFoundException
    @ExceptionHandler(BuyerNotFoundException.class)
    public ResponseEntity<Object> handleBuyerNotFoundException(BuyerNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // Shows the path of the error

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handle BuyerEmailAlreadyExistsException
    @ExceptionHandler(BuyerEmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleBuyerEmailAlreadyExistsException(BuyerEmailAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
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
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value()); // This should be 403
        body.put("error", "Forbidden");
        body.put("message", "Access Denied: You do not have the necessary permissions to access this resource.");
        body.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // Handle all other exceptions globally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred.");
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

