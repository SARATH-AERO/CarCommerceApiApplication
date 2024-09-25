package com.hcltech.car_commerce_api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
    private String message;
    private String jwtToken;
}
