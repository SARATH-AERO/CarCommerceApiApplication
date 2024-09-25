package com.hcltech.car_commerce_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
public class CarDto
{
    private int id;
    private String carName;
    private String model;
    private String brand;
    private Integer manufacturerYear;
    private String colour;
    private double price;
    @NotNull(message = "EngineNumber cannot be null")
    @NotBlank(message = "EngineNumber cannot be empty")
    @Size(min = 17, max = 17, message = "EngineNumber must be exactly 17 characters long")
    private String engineNumber;
}