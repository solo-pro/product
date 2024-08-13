package com.ecommer.product.response;

public record CategoryResponse(
        long id,
        String name,
        String description,
        Long createdTimeStamps,
        Long updatedTimeStamps
) {
}
