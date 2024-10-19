package com.springBoot.fruits_ecommerce.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.fruits_ecommerce.DTO.CartDetailsDTO;
import com.springBoot.fruits_ecommerce.models.AddToCartRequest;
import com.springBoot.fruits_ecommerce.models.Cart;
import com.springBoot.fruits_ecommerce.services.CartService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/details")
    public ResponseEntity<CartDetailsDTO> getCartDetails(@RequestParam Long userId) {

        CartDetailsDTO cartDetails = cartService.getCartDetails(userId);
        return ResponseEntity.ok(cartDetails);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequest) {

        Cart cart = cartService.addProductToCart(
                addToCartRequest.getUserId(),
                addToCartRequest.getProductId(),
                addToCartRequest.getQuantity());

        return ResponseEntity.ok(cart);
    }

}
