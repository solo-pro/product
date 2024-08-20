package com.ecommer.product.service;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
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
        return categoryRepository.findById(input.categoryId())
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .flatMap(category -> {
                    Product addProduct = Product.builder()
                            .name(input.name())
                            .price(input.price())
                            .stock(input.stock())
                            .mainImage(input.mainImage())
                            .description(input.description())
                            .categoryId(input.categoryId())
                            .build();
                    return productRepository.save(addProduct);
                });
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return productRepository.query(productQuery.getSqlQueryProductById(id)).one();
    }

    @Override
    public Mono<Product> updateProduct(ProductUpdateInput input) {
        Product updateProduct = Product.builder()
                .name(input.name())
                .price(input.price())
                .stock(input.stock())
                .mainImage(input.mainImage())
                .description(input.description())
                .categoryId(input.categoryId())
                .id(input.id())
                .build();
        return productRepository.save(updateProduct);
    }

    @Override
    public Mono<Void> deleteProductById(long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                .flatMap(product -> {
                    product.delete();
                    productRepository.save(product);
                    return Mono.empty();
                });

    }



}
