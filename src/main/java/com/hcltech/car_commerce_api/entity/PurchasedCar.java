package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class PurchasedCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String carName;

    @NonNull
    private String brand;

    @NonNull
    private String model;

    @Column(length = 4,nullable = false)
    private Integer manufacturerYear;

    @NonNull
    private String colour;

    @NonNull
    private double price;

    @Column(length = 17, nullable = false)
    private String engineNumber;

    @Column(updatable = false)
    private LocalDateTime purchasedDate;

    @PrePersist
    protected void onCreate(){
        purchasedDate = LocalDateTime.now();
    }
    
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
