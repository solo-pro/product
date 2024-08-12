package com.ecommer.product.request;

import com.ecommer.product.entity.Product;

public record ProductRequest(
        String name,
        String description,
        Long price,
        Long stock,
        Long categoryId
) {
    public ProductRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price cannot be null or less than or equal to 0");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be null or less than 0");
        }
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Category ID cannot be null or less than or equal to 0");
        }
    }
    public Product toEntity() {
        return new Product(null, name, price, stock, null, description, categoryId, null, false, null, null);
    }
}
