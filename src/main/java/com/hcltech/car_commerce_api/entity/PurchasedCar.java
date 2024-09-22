package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchasedCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String brand;

    @NonNull
    private String model;

    @Column(length = 4,nullable = false)
    private int manufacturerYear;

    @NonNull
    private String color;

    @NonNull
    private String chaseNumber;

    @NonNull
    private double price;

    @NonNull
    private String seller;


}
