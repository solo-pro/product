package com.ecommer.product.response;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        Long stock,
        String mainImage,
        Long createdTimeStamps,
        Long updatedTimeStamps,
        CategoryResponse category
){
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getMainImage(),
                product.getCreatedTimeStamps(),
                product.getUpdatedTimeStamps(),
                null
        );
    }
    public static ProductResponse from(Product product, Category category) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getMainImage(),
                product.getCreatedTimeStamps(),
                product.getUpdatedTimeStamps(),
                CategoryResponse.from(category)
        );
    }
}
