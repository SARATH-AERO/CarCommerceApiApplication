package com.hcltech.car_commerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BuyerDTO {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Firstname cannot be null")
    private String firstName;

    @NotNull(message = "Lastname cannot be null")
    private String lastName;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits and contain only numbers")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Postal code cannot be null")
    @Pattern(regexp = "^\\d{6}$", message = "Postal Code should be 6 digits and contain only numbers")
    private String postalCode;

    @NotNull(message = "License number cannot be null")
    private String licenseNumber;
}
