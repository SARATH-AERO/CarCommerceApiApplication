package com.hcltech.car_commerce_api.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
    
    @Bean
    public ModelMapper modelMapperConfig() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(context -> {
            Object sourceValue = context.getSource();
            if (sourceValue instanceof String) {
                return !((String) sourceValue).isEmpty();
            }
            return sourceValue != null;
        });

        return modelMapper;
    }
}
