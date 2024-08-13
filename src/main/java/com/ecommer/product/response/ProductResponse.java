package com.ecommer.product.response;

public record ProductResponse(
        long id,
        String name,
        String description,
        long price,
        long stock,
        String mainImage,
        Long createdTimeStamps,
        Long updatedTimeStamps,
        CategoryResponse category
) {
}
