package com.ecommer.product.service;

import com.ecommer.product.codegen.types.ProductInput;
import com.ecommer.product.codegen.types.ProductUpdateInput;
import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.exception.ProductNotFound;
import com.ecommer.product.queries.ProductQuery;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductQuery productQuery;

    @Override
    public Flux<ProductResponse> getProductsByFilters(String name, Integer overprice, Integer underprice, Integer categoryId, Integer page, Integer size) {
        return productRepository
                .query(productQuery.getSqlQueryProductConditions(name, overprice, underprice, categoryId, (long) page, size))
                .all()
                .map(ProductResponse::from);
    }

    @Override
    public Flux<ProductResponse> getProductsAndCategoryByFilters(String name, Integer overprice, Integer underprice, Integer categoryId, Integer page, Integer size) {
        return categoryRepository.findAll()
                .collectMap(Category::getId)
                .flatMapMany(categoryMap ->
                        productRepository
                                .query(productQuery.getSqlQueryProductConditions(name, overprice, underprice, categoryId, (long) page, size))
                                .all()
                                .map(product -> ProductResponse.from(product, categoryMap.get(product.getCategoryId())))
                );
    }



    @Override
    public Mono<Product> addProduct(ProductInput input) {
        return categoryRepository.findById(Long.parseLong(input.getCategoryId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .flatMap(category -> {
                    Product addProduct = Product.builder()
                            .name(input.getName())
                            .price(input.getPrice())
                            .stock(input.getStock())
                            .mainImage(input.getMainImage())
                            .description(input.getDescription())
                            .categoryId(Long.parseLong(input.getCategoryId()))
                            .build();
                    return productRepository.save(addProduct);
                });
    }

    @Override
    public Mono<ProductResponse> getProductById(Long id) {
        return productRepository.query(productQuery.getSqlQueryProductById(id)).one()
                .switchIfEmpty(Mono.error(new ProductNotFound(id)))
                .flatMap(product -> categoryRepository.findById(product.getCategoryId())
                        .map(category -> ProductResponse.from(product, category))
                );
    }

    @Override
    public Mono<Product> updateProduct(ProductUpdateInput input) {
        return productRepository.findById(Long.parseLong(input.getId()))
                .switchIfEmpty(Mono.error(new ProductNotFound(Long.parseLong(input.getId()))))
                .flatMap(product -> {
                    product.update(input);
                    return productRepository.save(product);
                });
    }

    @Override
    public Mono<Void> deleteProductById(long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFound(id)))
                .flatMap(product -> {
                    product.delete();
                    productRepository.save(product);
                    return Mono.empty();
                });

    }



}
