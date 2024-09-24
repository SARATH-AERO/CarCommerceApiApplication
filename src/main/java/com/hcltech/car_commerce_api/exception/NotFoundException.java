package com.hcltech.car_commerce_api.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(String message) {

        super(String.format("%s not Found.", message));
    }

}
