package com.hcltech.car_commerce_api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBuyerDto {

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\d{10}$|^$", message = "Phone number should be 10 digits and contain only numbers, or can be empty")
    private String phoneNumber;

    private String address;

    @Pattern(regexp = "^\\d{6}$|^$", message = "Postal Code should be 6 digits and contain only numbers, or can be empty")
    private String postalCode;

    @Pattern(regexp = "^\\d{15}$|^$", message = "License number must be exactly 15 characters long, or can be empty")
    private String licenseNumber;

    private String gender;

}
