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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.ecommer.product.entity.QCategory.category;
import static com.ecommer.product.entity.QProduct.product;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, ProductInputBooleanExpression, ProductDao {
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

    @Override
    public Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductConditions(String name, Integer overprice, Integer underprice, Integer categoryId, long page, Integer size) {
        return sqlQuery -> sqlQuery.select(product)
                .from(product)
                .where(containsName(name)
                        , goePrice(underprice)
                        , loePrice(overprice)
                        , eqCategoryId(categoryId)
                        , product.deleted.eq(false)
                )
                .join(category)
                .on(product.categoryId.eq(category.id))
                .orderBy(product.id.desc())
                .offset(page * size)
                .limit(size);
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
    public Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductById(Long id) {
        return sqlQuery -> sqlQuery.select(product)
                .from(product)
                .where(product.id.eq(id), product.deleted.eq(false));
    }
    @Override
    public Mono<Product> getProductById(Long id) {
        return productRepository.query(getSqlQueryProductById(id)).one();
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


    @Override
    public BooleanExpression loePrice(Integer overprice) {
        if (overprice != null) {
            return product.price.loe(overprice);
        }
        return null;
    }

    @Override
    public BooleanExpression goePrice(Integer underprice) {
        if (underprice != null) {
            return product.price.goe(underprice);
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
    public BooleanExpression eqCategoryId(Integer categoryId) {
        if (categoryId != null) {
            return product.categoryId.eq(Long.valueOf(categoryId));
        }
        return null;
    }
}
