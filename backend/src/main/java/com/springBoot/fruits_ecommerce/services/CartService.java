package com.springBoot.fruits_ecommerce.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.DTO.CartDetailsDTO;
import com.springBoot.fruits_ecommerce.DTO.CartItemDTO;
import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;

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

    public CartDetailsDTO getCartDetails(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        BigDecimal subtotal = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingCost = BigDecimal.valueOf(cart.getShippingCost());
        BigDecimal total = subtotal.add(shippingCost);

        List<CartItemDTO> items = cart.getCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getProduct().getImagePath(),
                        item.getQuantity(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .collect(Collectors.toList());

        return new CartDetailsDTO(items, subtotal, shippingCost, total);
    }
}
