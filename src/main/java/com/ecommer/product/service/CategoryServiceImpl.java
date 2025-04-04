package com.ecommer.product.service;

import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Flux<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().map(CategoryResponse::from);
    }
}
