package com.hcltech.car_commerce_api.exception;
import lombok.Getter;

@Getter
public class JwtTokenException  extends RuntimeException{
    private String detail;

    public JwtTokenException(String detail) {
        super("JWT token " +detail);
    }

}
