package com.hcltech.car_commerce_api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBuyerDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private String licenseNumber;
    private List<PurchasedCarDto> purchasedCarsList;
}