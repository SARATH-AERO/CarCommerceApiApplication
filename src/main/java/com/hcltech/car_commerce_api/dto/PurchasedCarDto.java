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
    private int manufacturerYear;
    private String colour;
    private double price;
    private String engineNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchasedDate;
}