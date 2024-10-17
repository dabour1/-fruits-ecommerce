package com.springBoot.fruits_ecommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.springBoot.fruits_ecommerce.models.PaginatedProducts;
import com.springBoot.fruits_ecommerce.models.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "content", target = "products")
    @Mapping(source = "number", target = "currentPage")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalItems")
    PaginatedProducts toPaginatedProducts(Page<Product> page);
}
