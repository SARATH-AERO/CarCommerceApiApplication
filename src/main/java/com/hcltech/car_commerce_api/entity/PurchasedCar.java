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
    private String brand;
    private String model;
    @Column(length = 4)
    private int manufacturerYear;
    private String color;
    private String chaseNumber;
    private double price;
    private String seller;

//    @ManyToOne
//    @JoinColumn(name = "buyer_id")
//    private Buyer buyer;

}
