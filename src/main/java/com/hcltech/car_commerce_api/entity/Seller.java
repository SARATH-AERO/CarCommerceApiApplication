package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true,nullable = false)  // Exception
    private String email;
    private String firstName;
    private String lastName;
    @Column(length = 10,nullable = false)
    private String phoneNumber;
    private String address;
    private String city;
    @Column(length = 6,nullable = false)
    private String postalCode;

    @Basic(optional = false)
    // @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @PrePersist
    protected void onCreate() {
        modifiedDate = new Date(); // Set modifiedDate when the entity is created
    }




}
