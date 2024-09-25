package com.hcltech.car_commerce_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseSellerDto {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private List<CarDto> carList;

}
