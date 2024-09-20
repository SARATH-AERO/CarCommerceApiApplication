package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true,nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    @Column(length = 10,nullable = false)
    private String phoneNumber;
    private String address;
    private String city;
    @Column(length = 6, nullable = false)
    private String postalCode;
    @Column(length = 15,nullable = false)
    private String licenseNumber;

    @Basic(optional = false)
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @PrePersist
    protected void onCreate(){
        modifiedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        modifiedDate = new Date();
    }

    @OneToMany()
    @JoinColumn(name = "buyer_id")
    private List<PurchasedCar> purchasedCarsList;
}
