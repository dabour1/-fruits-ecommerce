package com.springBoot.fruits_ecommerce.models;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddProductRequest {
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotBlank(message = "Unit is required")
    @NotNull(message = "Unit cannot be null")
    private String unit;
    @NotNull(message = "Quantity is required")
    @NotBlank(message = "Quantity is required")
    private Integer quantity;
    @NotNull(message = "Price is required")
    @NotBlank(message = "Price is required")
    private BigDecimal price;
    @NotBlank(message = "Description is required")
    @NotNull(message = "Description cannot be null")
    private String description;
    @NotNull(message = "Image is required")
    @NotBlank(message = "Image cannot be null")
    private MultipartFile image;

}
