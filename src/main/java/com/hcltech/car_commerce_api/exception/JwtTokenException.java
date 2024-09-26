package com.hcltech.car_commerce_api.exception;

public class JwtTokenException  extends RuntimeException{

    public JwtTokenException(String detail) {
        super("JWT token " +detail);
    }

}
