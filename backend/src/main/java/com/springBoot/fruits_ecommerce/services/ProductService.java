package com.springBoot.fruits_ecommerce.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;
import com.springBoot.fruits_ecommerce.models.AddProductRequest;
import com.springBoot.fruits_ecommerce.models.Product;
import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageService imageService;

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Product createProduct(AddProductRequest addProductRequest) {
        if (!checkNameUniques(addProductRequest.getName())) {
            throw new IllegalArgumentException("Product name must be unique");
        }
        try {
            String imageName = saveProductImage(addProductRequest);
            Product product = buildProduct(addProductRequest, imageName);
            return saveProduct(product);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating product: " + e.getMessage(), e);
        }
    }

    public Product updateProduct(Long id, AddProductRequest request) {
        Product product = getProductById(id);
        validateUniqueProductName(id, request.getName());

        if (isImageUpdateRequested(request)) {
            handleImageUpdate(product, request);
        } else {
            updateProductDetails(product, request);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {

        Product product = getProductById(productId);
        String imagePath = product.getImagePath();

        productRepository.delete(product);

        deleteImageFile(imagePath);
    }

    private void validateUniqueProductName(Long id, String productName) {
        Optional<Product> productOpt = findProductByName(productName);
        if (productOpt.isPresent() && !productOpt.get().getId().equals(id)) {
            throw new IllegalArgumentException("Product name must be unique");
        }
    }

    private boolean checkNameUniques(String name) {
        Optional<Product> existsProduct = productRepository.findFirstByName(name);
        return !existsProduct.isPresent();
    }

    private Optional<Product> findProductByName(String name) {
        return productRepository.findFirstByName(name);
    }

    private boolean isImageUpdateRequested(AddProductRequest request) {
        return request.getImage() != null && !request.getImage().isEmpty();
    }

    private void handleImageUpdate(Product product, AddProductRequest request) {
        deleteImageFile(product.getImagePath());
        String newImagePath = saveProductImage(request);
        updateProductImagePath(product, newImagePath);
    }

    private void updateProductImagePath(Product product, String newImagePath) {
        product.setImagePath(newImagePath);
    }

    private void updateProductDetails(Product product, AddProductRequest request) {
        product.setName(request.getName());
        product.setUnit(request.getUnit());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
    }

    private String saveProductImage(AddProductRequest addProductRequest) {
        return imageService.saveImage(addProductRequest.getImage());
    }

    private Product buildProduct(AddProductRequest addProductRequest, String imageName) {
        Product product = new Product();
        product.setName(addProductRequest.getName());
        product.setUnit(addProductRequest.getUnit());
        product.setPrice(addProductRequest.getPrice());
        product.setQuantity(addProductRequest.getQuantity());
        product.setDescription(addProductRequest.getDescription());
        product.setImagePath(imageName);
        return product;
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    private void deleteImageFile(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while deleting image file: " + e.getMessage());
            }

        }
    }

}
