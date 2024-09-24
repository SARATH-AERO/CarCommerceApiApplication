package com.hcltech.car_commerce_api.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private String detail;

    public NotFoundException(String detail) {
        super(String.format("%s not Found.", detail));
    }

}
