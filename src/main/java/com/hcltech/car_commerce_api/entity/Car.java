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
}
