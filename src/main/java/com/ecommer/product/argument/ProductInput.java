package com.ecommer.product.argument;

import ch.qos.logback.core.util.StringUtil;

public record ProductInput(
        String name,
        String description,
        long price,
        long categoryId,
        long stock,
        String mainImage
) {
    public ProductInput {
        if (StringUtil.notNullNorEmpty(name)) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Product price is required");
        }
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Product category is required");
        }
        if (stock <= 0) {
            throw new IllegalArgumentException("Product stock is required");
        }
    }

}
