package com.hcltech.car_commerce_api.exception;

public class SellerEmailAlreadyExistsException extends RuntimeException
{
    public SellerEmailAlreadyExistsException(String message)
    {
        super(message);
    }
}
