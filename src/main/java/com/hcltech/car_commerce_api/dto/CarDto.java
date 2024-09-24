package com.hcltech.car_commerce_api.dto;

import lombok.*;
@Getter
@Setter
public class CarDto
{
    private Integer id;
    private String carName;
    private String model;
    private String brand;
    private String year;
    private String colour;
    private double price;
    private String engineNumber;
}