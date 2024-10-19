package com.springBoot.fruits_ecommerce.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private String image;
    private int quantity;
    private BigDecimal totalPrice;
}
