package com.hcltech.car_commerce_api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSellerDto {

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits and contain only numbers")
    private String phoneNumber;

    private String address;

    @Pattern(regexp = "^\\d{6}$", message = "Postal Code should be 6 digits and contain only numbers")
    private String postalCode;

    private String gender;

}
