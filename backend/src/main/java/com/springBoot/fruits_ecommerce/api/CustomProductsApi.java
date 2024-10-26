package com.springBoot.fruits_ecommerce.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.springBoot.fruits_ecommerce.models.AddProductRequest;
import com.springBoot.fruits_ecommerce.models.Product;

import jakarta.validation.Valid;

public interface CustomProductsApi extends ProductsApi {
    @Override
    ResponseEntity<Product> createProduct(
            @Valid @RequestParam(value = "name", required = true) String name,
            @Valid @RequestParam(value = "unit", required = true) String unit,
            @Valid @RequestParam(value = "price", required = true) Float price,
            @Valid @RequestParam(value = "quantity", required = true) Integer quantity,
            @Valid @RequestParam(value = "description", required = true) String description,
            @RequestPart(value = "image", required = false) MultipartFile image);

    @Override
    ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestParam(value = "name", required = true) String name,
            @Valid @RequestParam(value = "unit", required = true) String unit,
            @Valid @RequestParam(value = "price", required = true) Float price,
            @Valid @RequestParam(value = "quantity", required = true) Integer quantity,
            @Valid @RequestParam(value = "description", required = true) String description,
            @RequestPart(value = "image", required = false) MultipartFile image);
}
