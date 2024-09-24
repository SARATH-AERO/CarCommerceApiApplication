package com.hcltech.car_commerce_api.dto;

import com.hcltech.car_commerce_api.entity.Car;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
public class SellerDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NotNull(message = "Password cannot be null")
    private String password;
    private String address;
    private String city;
    private String postalCode;
    private List<Car> cars;
}
