package com.springBoot.fruits_ecommerce.models;

import java.math.BigDecimal;
import java.util.List;

import com.springBoot.fruits_ecommerce.DTO.CartItemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsResponse {

    private List<CartItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal total;
}
