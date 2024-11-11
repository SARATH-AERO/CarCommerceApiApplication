package com.hcltech.car_commerce_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedCarDto {
    private String carName;
    private String brand;
    private String model;
    private Integer manufacturerYear;
    private String colour;
    private double price;
    private String engineNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchasedDate;
    
    // new fields

    private Integer seatCapacity;

    private String fuelType;

    private String transmission;

    private float mileage;

    private String description;

    private String kmDriven;

    private String location;

    private String carVariant;

    private String hub;
}