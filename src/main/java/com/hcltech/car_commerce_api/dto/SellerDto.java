package com.hcltech.car_commerce_api.dto;

import com.hcltech.car_commerce_api.entity.Car;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellerDto {
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Firstname cannot be null")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;
    @NotNull(message = "Lastname cannot be null")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits and contain only numbers")
    private String phoneNumber;
    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits and contain only numbers")
    private String password;
    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotNull(message = "Postal code cannot be null")
    @NotBlank(message = "License number cannot be empty")
    @Pattern(regexp = "^\\d{6}$", message = "Postal Code should be 6 digits and contain only numbers")
    private String postalCode;
    private List<Car> cars;
}
