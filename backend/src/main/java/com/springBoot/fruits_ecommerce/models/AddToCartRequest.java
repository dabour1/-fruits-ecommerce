package com.springBoot.fruits_ecommerce.models;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddToCartRequest {

    @NotNull(message = "user Id can't be null")
    private Long userId;

    @NotNull(message = "product Id can't be null")
    private Long productId;
    @NotNull(message = "quantity can't be null")
    private Integer quantity;

}
