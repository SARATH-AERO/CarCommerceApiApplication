package com.hcltech.car_commerce_api.exception;


import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {

    private String detail;

    public AlreadyExistException(String detail) {

        super(String.format("%s already Exist.", detail));
    }

}
