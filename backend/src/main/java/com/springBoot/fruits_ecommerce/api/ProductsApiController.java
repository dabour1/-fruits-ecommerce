package com.springBoot.fruits_ecommerce.api;

import com.springBoot.fruits_ecommerce.mappers.MapRegistrationRequest;
import com.springBoot.fruits_ecommerce.mappers.ProductMapper;
import com.springBoot.fruits_ecommerce.models.AddProductRequest;
import com.springBoot.fruits_ecommerce.models.ErrorResponse;
import com.springBoot.fruits_ecommerce.models.PaginatedProducts;
import com.springBoot.fruits_ecommerce.models.Product;
import com.springBoot.fruits_ecommerce.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-08T05:28:38.614903300+03:00[Asia/Riyadh]", comments = "Generator version: 7.8.0")
@Controller
@RequestMapping("${openapi.productManagement.base-path:/api}")
public class ProductsApiController implements ProductsApi {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    private final NativeWebRequest request;

    @Autowired
    public ProductsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<PaginatedProducts> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> productsPage = productService.getAllProducts(page, size);
        // productsPage.getContent();

        PaginatedProducts paginatedProducts = productMapper.toPaginatedProducts(productsPage);

        return ResponseEntity.ok(paginatedProducts);
    }

    @Override
    public ResponseEntity<Product> createProduct(
            @Valid @RequestParam(value = "name", required = true) String name,
            @Valid @RequestParam(value = "unit", required = true) String unit,
            @Valid @RequestParam(value = "price", required = true) Float price,
            @Valid @RequestParam(value = "quantity", required = true) Integer quantity,
            @Valid @RequestParam(value = "description", required = true) String description,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        AddProductRequest request = new AddProductRequest(name, unit, quantity, BigDecimal.valueOf(price), description,
                image);
        Product product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);

    }

    @Override
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
            @Valid @RequestParam(value = "name", required = true) String name,
            @Valid @RequestParam(value = "unit", required = true) String unit,
            @Valid @RequestParam(value = "price", required = true) Float price,
            @Valid @RequestParam(value = "quantity", required = true) Integer quantity,
            @Valid @RequestParam(value = "description", required = true) String description,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        AddProductRequest request = new AddProductRequest(name, unit, quantity, BigDecimal.valueOf(price), description,
                image);
        Product updatedProduct = productService.updateProduct(id, request);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
