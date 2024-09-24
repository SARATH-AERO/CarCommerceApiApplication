package com.hcltech.car_commerce_api.exception;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {

    private String message;

    public AlreadyExistException(String message) {

        super(String.format("%s already Exist.", message));
    }

}
