package com.springBoot.fruits_ecommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;
import com.springBoot.fruits_ecommerce.models.AddToCartRequest;
import com.springBoot.fruits_ecommerce.models.Cart;
import com.springBoot.fruits_ecommerce.models.CartItem;

import com.springBoot.fruits_ecommerce.models.Product;

import com.springBoot.fruits_ecommerce.models.User;

import com.springBoot.fruits_ecommerce.repositorys.CartItemRepository;
import com.springBoot.fruits_ecommerce.repositorys.CartRepository;

import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;

import com.springBoot.fruits_ecommerce.repositorys.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${app.shipping.cost}")
    private Double shippingCost;

    @Transactional
    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setShippingCost(shippingCost);
                    newCart.setDiscount(0.0);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),
                productId);

        if (existingCartItem.isPresent()) {

            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }
}
