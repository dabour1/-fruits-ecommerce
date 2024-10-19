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
import com.springBoot.fruits_ecommerce.models.Cart;
import com.springBoot.fruits_ecommerce.services.CartService;

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

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_CLIENT')")

    public ResponseEntity<Cart> addProductToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        Cart cart = cartService.addProductToCart(userId, productId, quantity);

        return ResponseEntity.ok(cart);
    }

}
