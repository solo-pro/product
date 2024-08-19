package com.ecommer.product.service;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.ecommer.product.entity.QProduct.product;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, ProductInputBooleanExpression {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Flux<ProductResponse> getProductsByFilters(String name, Integer overprice, Integer underprice, Integer categoryId, Integer page, Integer size) {
        return productRepository
                .query(getSqlQueryProductConditions(name, overprice, underprice, categoryId, (long) page, size))
                .all()
                .map(ProductResponse::from);
    }

    @Override
    public Flux<ProductResponse> getProductsAndCategoryByFilters(String name, Integer overprice, Integer underprice, Integer categoryId, Integer page, Integer size) {
        return categoryRepository.findAll()
                .collectMap(Category::getId)
                .flatMapMany(categoryMap ->
                        productRepository
                                .query(getSqlQueryProductConditions(name, overprice, underprice, categoryId, (long) page, size))
                                .all()
                                .map(product -> ProductResponse.from(product, categoryMap.get(product.getCategoryId())))
                );
    }

    public @NotNull Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductConditions(String name, Integer overprice, Integer underprice, Integer categoryId, long page, Integer size) {
        return sqlQuery -> sqlQuery.select(product)
                .from(product)
                .where(containsName(name)
                        , goePrice(overprice)
                        , loePrice(underprice)
                        , eqCategoryId(categoryId)
                )
                .orderBy(product.id.desc())
                .offset(page * size)
                .limit(size);
    }

    @Override
    public Mono<Product> addProduct(ProductInput input) {
        Product addProduct = Product.builder()
                .name(input.name())
                .price(input.price())
                .stock(input.stock())
                .mainImage(input.mainImage())
                .description(input.description())
                .categoryId(input.categoryId())
                .build();
        return productRepository.save(addProduct);
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
        return productRepository.deleteById(id);
    }


    @Override
    public BooleanExpression loePrice(Integer underprice) {
        if (underprice != null) {
            return product.price.loe(underprice);
        }
        return null;
    }

    @Override
    public BooleanExpression goePrice(Integer overprice) {
        if (overprice != null) {
            return product.price.goe(overprice);
        }
        return null;
    }

    @Override
    public BooleanExpression containsName(String name) {
        if (name != null) {
            return product.name.containsIgnoreCase(name);
        }
        return null;
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    @Override
    public BooleanExpression eqCategoryId(Integer categoryId) {
        if (categoryId != null) {
            return product.categoryId.eq(Long.valueOf(categoryId));
        }
        return null;
    }
}
