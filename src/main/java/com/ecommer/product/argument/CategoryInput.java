package com.ecommer.product.argument;

import ch.qos.logback.core.util.StringUtil;

public record CategoryInput(
        String name,
        String description
) {
    public CategoryInput {
        if (StringUtil.notNullNorEmpty(name)) {
            throw new IllegalArgumentException("Category name is required");
        }
    }
}
