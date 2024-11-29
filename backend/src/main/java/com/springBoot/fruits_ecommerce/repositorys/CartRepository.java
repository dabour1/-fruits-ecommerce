package com.springBoot.fruits_ecommerce.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}
