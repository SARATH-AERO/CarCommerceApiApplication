package com.hcltech.car_commerce_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedCarDTO {

    private long id;
    private String carName;
    private String brand;
    private String model;
    private int manufacturerYear;
    private String colour;
    private double price;
    private String engineNumber;
    private LocalDateTime purchasedDate;

    // Getters and setters
}