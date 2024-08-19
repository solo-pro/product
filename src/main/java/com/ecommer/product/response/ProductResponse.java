package com.ecommer.product.response;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse{
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private String mainImage;
    private Long createdTimeStamps;
    private Long updatedTimeStamps;
    private CategoryResponse category;
//    private Long categoryId;
//    private String categoryName;
//    private String categoryDescription;
//    private Long categoryCreatedTimeStamps;
//    private Long categoryUpdatedTimeStamps;
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
