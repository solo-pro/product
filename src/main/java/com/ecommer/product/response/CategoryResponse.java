package com.ecommer.product.response;

import com.ecommer.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long createdTimeStamps;
    private Long updatedTimeStamps;

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
