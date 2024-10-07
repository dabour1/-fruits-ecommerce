package com.springBoot.fruits_ecommerce.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;
import com.springBoot.fruits_ecommerce.models.AddProductRequest;
import com.springBoot.fruits_ecommerce.models.Product;
import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageService imageService;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product createProduct(AddProductRequest addProductRequest) {
        try {
            String imageName = imageService.saveImage(addProductRequest.getImage());
            Product product = new Product();
            product.setName(addProductRequest.getName());
            product.setUnit(addProductRequest.getUnit());
            product.setPrice(addProductRequest.getPrice());
            product.setQuantity(addProductRequest.getQuantity());
            product.setDescription(addProductRequest.getDescription());
            product.setImagePath(imageName);

            Product savedProduct = productRepository.save(product);
            return savedProduct;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating product: " + e.getMessage());
        }

    }

    public void deleteProduct(Long productId) {

        Product product = getProductById(productId);
        String imagePath = product.getImagePath();

        productRepository.delete(product);

        deleteImageFile(imagePath);
    }

    private void deleteImageFile(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    boolean deleted = imageFile.delete();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while deleting image file: " + e.getMessage());
            }

        }
    }

}
