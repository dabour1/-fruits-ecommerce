package com.springBoot.fruits_ecommerce.models;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    private String description;

    private MultipartFile image;

}
