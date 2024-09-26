package com.hcltech.car_commerce_api.exception;

public class NotFoundException extends RuntimeException {


    public NotFoundException(String detail) {
        super(String.format("%s not Found.", detail));
    }

}
