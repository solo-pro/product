package com.ecommer.product.argument;

public record ProductUpdateInput(
        long id,
        String name,
        String description,
        Double price,
        Long categoryId,
        Long stock,
        String mainImage,
        Boolean deleted
) {
    public ProductUpdateInput {
        if (id <= 0) {
            throw new IllegalArgumentException("Product id is required");
        }
    }
}
