package com.hcltech.car_commerce_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginDto {
    private String message;
    private String jwtToken;
}
