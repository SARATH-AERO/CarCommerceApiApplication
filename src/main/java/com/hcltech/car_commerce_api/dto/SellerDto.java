package com.hcltech.car_commerce_api.dto;

import com.hcltech.car_commerce_api.entity.Cars;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NotNull(message = "Password cannot be null")
    private String password;
    private String address;
    private String city;
    private String postalCode;
    private List<Cars> cars;
}
