package com.hcltech.car_commerce_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BuyerDto {
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Firstname cannot be null")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;

    @NotNull(message = "Lastname cannot be null")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits and contain only numbers")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Postal code cannot be null")
    @NotBlank(message = "Postal code cannot be empty")
    @Pattern(regexp = "^\\d{6}$", message = "Postal Code should be 6 digits and contain only numbers")
    private String postalCode;

    @NotNull(message = "License number cannot be null")
    @NotBlank(message = "License number cannot be empty")
    @Size(min = 15, max = 15, message = "License number must be exactly 15 characters long")
    private String licenseNumber;
}
