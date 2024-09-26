package com.springBoot.fruits_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }
}
