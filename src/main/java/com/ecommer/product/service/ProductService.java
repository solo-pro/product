package com.ecommer.product.service;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Product;
import com.ecommer.product.response.ProductResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<ProductResponse> getProductsByFilters(
            String name
            , Integer overprice
            , Integer underprice
            , Integer categoryId
            , Integer page
            , Integer size
    );
    Mono<Product> getProductById(Long id);
    Mono<Product> addProduct(ProductInput input);
    Mono<Product> updateProduct(ProductUpdateInput input);
    Mono<Void> deleteProductById(long id);
    Flux<ProductResponse> getProductsAndCategoryByFilters(
            String name
            , Integer overprice
            , Integer underprice
            , Integer categoryId
            , Integer page
            , Integer size
    );
}
