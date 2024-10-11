package com.springBoot.fruits_ecommerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.springBoot.fruits_ecommerce.exception.ResourceNotFoundException;
import com.springBoot.fruits_ecommerce.models.AddProductRequest;
import com.springBoot.fruits_ecommerce.models.Product;
import com.springBoot.fruits_ecommerce.repositorys.ProductRepository;
import com.springBoot.fruits_ecommerce.services.ImageService;
import com.springBoot.fruits_ecommerce.services.ProductService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageService imageService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setImagePath("/test/image.jpg");
    }

    @Test
    void testGetProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> productPage = productService.getAllProducts(0, 10);

        assertEquals(1, productPage.getTotalElements());
        assertEquals("Test Product", productPage.getContent().get(0).getName());
    }

    @Test
    void testCreateProduct_Success() {
        AddProductRequest request = new AddProductRequest();
        request.setName("New Product");

        MultipartFile imageFile = new MockMultipartFile("image", "newImage.jpg", "image/jpeg", "image data".getBytes());
        request.setImage(imageFile);

        when(productRepository.findFirstByName(anyString())).thenReturn(Optional.empty());
        when(imageService.saveImage(any(MultipartFile.class))).thenReturn("/path/to/image.jpg");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(request);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        verify(productRepository).save(any(Product.class));
        verify(imageService).saveImage(any(MultipartFile.class));
    }

    @Test
    void testCreateProduct_NameNotUnique() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Test Product");

        when(productRepository.findFirstByName("Test Product")).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Updated Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findFirstByName(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, request);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_DeletesImage() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(any(Product.class));
        verify(imageService).deleteImageFile(anyString());
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any(Product.class));
    }
}
