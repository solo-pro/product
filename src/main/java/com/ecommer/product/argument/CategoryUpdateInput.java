package com.ecommer.product.argument;

import ch.qos.logback.core.util.StringUtil;

public record CategoryUpdateInput(
        long id,
        String name,
        String description
) {
    public CategoryUpdateInput {
        if (StringUtil.notNullNorEmpty(name)) {
            throw new IllegalArgumentException("Name is required");
        }
    }
}
