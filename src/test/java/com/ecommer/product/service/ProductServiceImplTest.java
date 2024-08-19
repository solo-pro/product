package com.ecommer.product.service;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QProduct;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(1000)
                .stock(10)
                .mainImage("image.png")
                .description("Test Description")
                .categoryId(1L)
                .build();
    }

    @Test
    void testGetProductsByFilters() {
        // Mock RowsFetchSpec and its all() method
        RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
        when(productRepository.query(any())).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

        Flux<ProductResponse> products = productService.getProductsByFilters("Test", 500, 1500, 1, 0, 10);

        StepVerifier.create(products)
                .expectNext(ProductResponse.from(product))
                .verifyComplete();

        verify(productRepository, times(1)).query(any());
        verify(rowsFetchSpec, times(1)).all();
    }


    @Test
    void testGetProductsByFilters_AllConditions() {
        RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
        when(productRepository.query(any())).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

        Flux<ProductResponse> products = productService.getProductsByFilters("Test", 500, 1500, 1, 0, 10);

        StepVerifier.create(products)
                .expectNext(ProductResponse.from(product))
                .verifyComplete();

        verify(productRepository, times(1)).query(any());
        verify(rowsFetchSpec, times(1)).all();
    }

    @Test
    void testGetSqlQueryProductConditions_AllConditions() {
        // Mock SQLQuery and its methods
        SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
        QProduct qProduct = QProduct.product;

        // Arrange
        Predicate containsName = qProduct.name.containsIgnoreCase("Test");
        Predicate goePrice = qProduct.price.goe(500);
        Predicate loePrice = qProduct.price.loe(1500);
        Predicate eqCategoryId = qProduct.categoryId.eq(1L);

        // Ensure that where method is stubbed with specific arguments
        when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
        when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
        when(sqlQueryMock.where(containsName, goePrice, loePrice, eqCategoryId)).thenReturn(sqlQueryMock);
        when(sqlQueryMock.orderBy(qProduct.id.desc())).thenReturn(sqlQueryMock);
        when(sqlQueryMock.offset(anyLong())).thenReturn(sqlQueryMock);
        when(sqlQueryMock.limit(anyLong())).thenReturn(sqlQueryMock);

        // Act
        Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductConditions(
                "Test", 500, 1500, 1, 0, 10);
        SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

        // Assert
        verify(sqlQueryMock, times(1)).select(qProduct);
        verify(sqlQueryMock, times(1)).from(qProduct);
        verify(sqlQueryMock, times(1)).where(containsName, goePrice, loePrice, eqCategoryId);
        verify(sqlQueryMock, times(1)).orderBy(qProduct.id.desc());
        verify(sqlQueryMock, times(1)).offset(0L);
        verify(sqlQueryMock, times(1)).limit(10L);

        // Verify the function returned the expected query
        assertSame(sqlQueryMock, resultQuery);
    }



    @Test
    void testGetProductsByFilters_OnlyName() {
        RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
        when(productRepository.query(any())).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

        Flux<ProductResponse> products = productService.getProductsByFilters("Test", null, null, null, 0, 10);

        StepVerifier.create(products)
                .expectNext(ProductResponse.from(product))
                .verifyComplete();

        verify(productRepository, times(1)).query(any());
        verify(rowsFetchSpec, times(1)).all();
    }

    @Test
    void testGetProductsByFilters_NoConditions() {
        RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
        when(productRepository.query(any())).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

        Flux<ProductResponse> products = productService.getProductsByFilters(null, null, null, null, 0, 10);

        StepVerifier.create(products)
                .expectNext(ProductResponse.from(product))
                .verifyComplete();

        verify(productRepository, times(1)).query(any());
        verify(rowsFetchSpec, times(1)).all();
    }

    @Test
    void testAddProduct() {
        ProductInput input = new ProductInput("New Product", "New Description", 20, 1L, 1500, "new_image.png");
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> savedProduct = productService.addProduct(input);

        StepVerifier.create(savedProduct)
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        ProductUpdateInput input = new ProductUpdateInput(1L, "Updated Product", "Updated Description", 20L, 1L, 2000L, "updated_image.png", false);
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> updatedProduct = productService.updateProduct(input);

        StepVerifier.create(updatedProduct)
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.deleteById(anyLong())).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteProductById(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        Mono<Product> foundProduct = productService.getProductById(1L);

        StepVerifier.create(foundProduct)
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.empty());

        Mono<Product> foundProduct = productService.getProductById(1L);

        StepVerifier.create(foundProduct)
                .verifyComplete();

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testLoePrice() {
        BooleanExpression booleanExpression = productService.loePrice(1000);
        assertNotNull(booleanExpression);
    }
    @Test
    void testGoePrice() {
        BooleanExpression booleanExpression = productService.goePrice(1000);
        assertNotNull(booleanExpression);
    }
    @Test
    void testContainsName() {
        BooleanExpression booleanExpression = productService.containsName("Test");
        assertNotNull(booleanExpression);
    }
    @Test
    void testContainsName_Null() {
        BooleanExpression booleanExpression = productService.containsName(null);
        assertNull(booleanExpression);
    }
    @Test
    void testLoePrice_Null() {
        BooleanExpression booleanExpression = productService.loePrice(null);
        assertNull(booleanExpression);
    }
    @Test
    void testGoePrice_Null() {
        BooleanExpression booleanExpression = productService.goePrice(null);
        assertNull(booleanExpression);
    }
    @Test
    void testEqCategoryId() {
        BooleanExpression booleanExpression = productService.eqCategoryId(1);
        assertNotNull(booleanExpression);
    }
    @Test
    void testEqCategoryId_Null() {
        BooleanExpression booleanExpression = productService.eqCategoryId(null);
        assertNull(booleanExpression);
    }



}
