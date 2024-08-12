package com.ecommer.product.request;

import com.ecommer.product.entity.Category;

public record CategoryRequest (
        String name,
        String description
){
    public CategoryRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }
    public Category toEntity() {
        return new Category(null, name, description, null, null);
    }
}
