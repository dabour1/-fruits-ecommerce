package com.springBoot.fruits_ecommerce.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBoot.fruits_ecommerce.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
