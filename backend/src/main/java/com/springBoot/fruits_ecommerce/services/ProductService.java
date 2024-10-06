package com.springBoot.fruits_ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;
import com.springBoot.fruits_ecommerce.models.Product;
import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;

public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public void save(Product product) {

    }

}
