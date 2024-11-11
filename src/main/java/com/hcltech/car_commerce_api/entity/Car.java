package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String carName;
    private String brand;
    private String model;
    @Column(length = 4,nullable = false)
    private Integer manufacturerYear;
    private String colour;
    private double price;
    @Column(length = 17, nullable = false)
    private String engineNumber;
    
    //new fields

    @Column(length = 1)
    private Integer seatCapacity;

    @Column(length = 7)
    private String fuelType;

    @Column(length = 7)
    private String transmission;

    private float mileage;

    private String description;

    private String kmDriven;

    private String location;

    private String carVariant;

    private String hub;
}
