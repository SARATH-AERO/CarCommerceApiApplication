package com.hcltech.car_commerce_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
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
    public ProblemDetail alreadyExistException(AlreadyExistException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,String.format("Already exist %s",
                        exception.getMessage()));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex){
//        String string = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() +"-" + e.getDefaultMessage()+"/").collect(Collectors.joining());
//           return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,string);
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,"Access Denied");
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ProblemDetail handleGlobalException(Exception e) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//
//    }
}

