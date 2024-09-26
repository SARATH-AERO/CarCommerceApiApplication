package com.hcltech.car_commerce_api.exception;

public class AlreadyExistException extends RuntimeException {


    public AlreadyExistException(String detail) {

        super(String.format("%s already Exist.", detail));
    }

}
