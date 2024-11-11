package com.hcltech.car_commerce_api.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ResponseCarDto {

    private Integer id;
    private String carName;
    private String brand;
    private String model;
    private Integer manufacturerYear;
    private String colour;
    private double price;
    private String engineNumber;
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
