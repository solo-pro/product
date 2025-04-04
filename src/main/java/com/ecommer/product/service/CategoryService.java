package com.ecommer.product.service;

import com.ecommer.product.response.CategoryResponse;
import reactor.core.publisher.Flux;


public interface CategoryService {
    Flux<CategoryResponse> getAllCategories(Integer page, Integer size);
}
