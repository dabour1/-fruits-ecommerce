package com.springBoot.fruits_ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.springBoot.fruits_ecommerce.models.PaginatedProducts;
import com.springBoot.fruits_ecommerce.models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "content", target = "products")
    @Mapping(source = "number", target = "currentPage")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalItems")
    PaginatedProducts toPaginatedProducts(Page<Product> page);
}
