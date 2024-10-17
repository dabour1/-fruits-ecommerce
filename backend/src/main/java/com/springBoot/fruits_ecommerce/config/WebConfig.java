package com.springBoot.fruits_ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL path /images/** to the file system location of the uploads folder
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/files/");
    }
}
