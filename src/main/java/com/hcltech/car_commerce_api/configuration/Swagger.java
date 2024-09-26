package com.hcltech.car_commerce_api.configuration;

import com.hcltech.car_commerce_api.common.CommonValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Authentication Service"))
                .addSecurityItem(new SecurityRequirement().addList(CommonValue.SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(CommonValue.SECURITY_SCHEME,new SecurityScheme().
                        name(CommonValue.SECURITY_SCHEME).type(SecurityScheme.Type.HTTP).
                        scheme("bearer").bearerFormat("JWT")));
    }
}