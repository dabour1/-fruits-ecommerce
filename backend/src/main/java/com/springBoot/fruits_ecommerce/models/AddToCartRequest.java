package com.springBoot.fruits_ecommerce.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddToCartRequest {

    @NotNull(message = "user Id can't be null")
    private int userId;

    @NotNull(message = "product Id can't be null")
    private int productId;
    @NotNull(message = "quantity can't be null")
    private int quantity;
}
