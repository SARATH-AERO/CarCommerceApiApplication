package com.hcltech.car_commerce_api.exception;

public class BuyerEmailAlreadyExistsException extends RuntimeException {
    public BuyerEmailAlreadyExistsException(String message) {
        super(message);
    }
}
