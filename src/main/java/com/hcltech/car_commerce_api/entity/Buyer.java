package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private long id; //local to your database

    @Column(unique = true,nullable = false)
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @Column(length = 10,nullable = false)
    private String phoneNumber;

    @NonNull
    private String address;

    @Column(length = 6, nullable = false)
    private String postalCode;

    @Column(length = 15,nullable = false)
    private String licenseNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id")
    private List<PurchasedCar> purchasedCarsList;
}
