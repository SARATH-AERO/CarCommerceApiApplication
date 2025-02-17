package com.hcltech.car_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    @Column(length = 6,nullable = false)
    private String postalCode;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        modifiedDate = new Date();
        createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        modifiedDate = new Date();
    }
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private List<Car> carList;


    private String gender;

}

