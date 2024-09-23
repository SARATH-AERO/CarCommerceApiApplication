package com.hcltech.car_commerce_api.exception;

public class SellerNotFoundException extends RuntimeException{
    private Integer id;
    public SellerNotFoundException(Integer id)
    {
        super(String.format("Seller id %d is not found",id));
        this.id=id;
    }

    public Integer getId() {
        return id;
    }
}
