package com.ecommer.product.fetchers;

import com.ecommer.product.response.CategoryResponse;
import com.ecommer.product.service.CategoryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@DgsComponent
@RequiredArgsConstructor
public class CategoryDataFetcher {
    private final CategoryService categoryService;

    @DgsQuery
    public Flux<CategoryResponse> categories() {
        return categoryService.getAllCategories();
    }
}
