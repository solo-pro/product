package com.ecommer.product.response;

import com.ecommer.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record CategoryResponse(
        Long id,
        String name,
        String description,
        Long createdTimeStamps,
        Long updatedTimeStamps
) {
    public static CategoryResponse from(Category category) {
        if(category == null) {
            return null;
        }
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedTimeStamps(),
                category.getUpdatedTimeStamps()
        );
    }
}
