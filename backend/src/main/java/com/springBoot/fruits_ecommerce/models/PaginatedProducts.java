package com.springBoot.fruits_ecommerce.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedProducts {
    private List<Product> products;
    private int currentPage;
    private int totalPages;
    private long totalItems;

}
