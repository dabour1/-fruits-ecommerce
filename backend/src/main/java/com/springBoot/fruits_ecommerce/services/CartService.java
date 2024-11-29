package com.springBoot.fruits_ecommerce.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.DTO.CartDetailsDTO;
import com.springBoot.fruits_ecommerce.DTO.CartItemDTO;
import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;

import com.springBoot.fruits_ecommerce.models.Cart;
import com.springBoot.fruits_ecommerce.models.CartDetailsResponse;
import com.springBoot.fruits_ecommerce.models.CartItem;

import com.springBoot.fruits_ecommerce.models.Product;

import com.springBoot.fruits_ecommerce.models.User;

import com.springBoot.fruits_ecommerce.repositorys.CartItemRepository;
import com.springBoot.fruits_ecommerce.repositorys.CartRepository;

import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;

import com.springBoot.fruits_ecommerce.repositorys.UserRepository;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

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
                User user = findUserById(userId);
                verifyUserAuthorization(userId);
                Product product = findProductById(productId);
                Cart cart = findOrCreateCart(user);
                updateCartItems(cart, product, quantity);
                return cartRepository.save(cart);
        }

        public CartDetailsResponse getCartDetails(Long userId) {
                Cart cart = findCartByUserId(userId);
                BigDecimal subtotal = calculateSubtotal(cart);
                BigDecimal shippingCost = calculateShippingCost(cart);
                BigDecimal total = subtotal.add(shippingCost);
                List<CartItemDTO> items = mapCartItemsToDTOs(cart);

                return new CartDetailsResponse(items, subtotal, shippingCost, total);
        }

        private Cart findCartByUserId(Long userId) {
                return cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Cart not found for user id: " + userId));
        }

        private BigDecimal calculateSubtotal(Cart cart) {
                return cart.getCartItems().stream()
                                .map(item -> item.getProduct().getPrice()
                                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private BigDecimal calculateShippingCost(Cart cart) {
                return BigDecimal.valueOf(cart.getShippingCost());
        }

        private List<CartItemDTO> mapCartItemsToDTOs(Cart cart) {
                return cart.getCartItems().stream()
                                .map(item -> new CartItemDTO(
                                                item.getProduct().getId(),
                                                item.getProduct().getName(),
                                                item.getProduct().getPrice(),
                                                item.getProduct().getImagePath(),
                                                item.getQuantity(),
                                                item.getProduct().getPrice()
                                                                .multiply(BigDecimal.valueOf(item.getQuantity()))))
                                .collect(Collectors.toList());
        }

        private User findUserById(Long userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        private void verifyUserAuthorization(Long userId) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();

                if (!username.equals(userRepository.findById(userId).get().getUsername())) {
                        throw new IllegalArgumentException("You do not have permission to add to this cart");
                }
        }

        private Product findProductById(Long productId) {
                return productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        }

        private Cart findOrCreateCart(User user) {
                return cartRepository.findByUserId(user.getId())
                                .orElseGet(() -> createNewCart(user));
        }

        private Cart createNewCart(User user) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setShippingCost(shippingCost);
                newCart.setDiscount(0.0);
                return cartRepository.save(newCart);
        }

        private void updateCartItems(Cart cart, Product product, Integer quantity) {
                Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),
                                product.getId());

                if (existingCartItem.isPresent()) {
                        updateExistingCartItem(existingCartItem.get(), quantity);
                } else {
                        addNewCartItem(cart, product, quantity);
                }
        }

        private void updateExistingCartItem(CartItem cartItem, Integer quantity) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        private void addNewCartItem(Cart cart, Product product, Integer quantity) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cart.getCartItems().add(cartItem);
        }

}
