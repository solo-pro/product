package com.ecommer.product.service;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QCategory;
import com.ecommer.product.entity.QProduct;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Mock
    private CategoryRepository categoryRepository;


    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    BooleanExpression deletedFalse = QProduct.product.deleted.eq(false);

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Category for electronics")
                .build();
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
    @Nested
    class GetProductsAndCategoryByFilters{
        @Test
        void allCondition() {
            // Arrange
            Flux<Category> categoryFlux = Flux.just(category);

            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);

            when(categoryRepository.findAll()).thenReturn(categoryFlux);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

            // Act
            Flux<ProductResponse> productResponses = productService.getProductsAndCategoryByFilters(
                    "Test", 100, 1000, 1, 0, 10);

            // Assert
            StepVerifier.create(productResponses)
                    .expectNextMatches(response ->
                            response.name().equals("Test Product") &&
                                    response.category().getName().equals("Electronics")
                    )
                    .verifyComplete();
        }
        @Test
        void noCondition() {
            // Arrange
            Flux<Category> categoryFlux = Flux.just(category);

            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);

            when(categoryRepository.findAll()).thenReturn(categoryFlux);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

            // Act
            Flux<ProductResponse> productResponses = productService.getProductsAndCategoryByFilters(
                    null, null, null, null, 0, 10);

            // Assert
            StepVerifier.create(productResponses)
                    .expectNextMatches(response ->
                            response.name().equals("Test Product") &&
                                    response.category().getName().equals("Electronics")
                    )
                    .verifyComplete();
        }
        @Test
        void onlyName() {
            // Arrange
            Flux<Category> categoryFlux = Flux.just(category);

            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);

            when(categoryRepository.findAll()).thenReturn(categoryFlux);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.all()).thenReturn(Flux.just(product));

            // Act
            Flux<ProductResponse> productResponses = productService.getProductsAndCategoryByFilters(
                    "Test", null, null, null, 0, 10);

            // Assert
            StepVerifier.create(productResponses)
                    .expectNextMatches(response ->
                            response.name().equals("Test Product") &&
                                    response.category().getName().equals("Electronics")
                    )
                    .verifyComplete();
        }
    }

    @Nested
    class GetProductsByFilters{

        @Test
        void allConditions() {
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
        void onlyName() {
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
        void noConditions() {
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
    }

    @Nested
    class GetSqlQueryProductConditions{
        @Test
        void allConditions() {
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
            when(sqlQueryMock.where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.join(QCategory.category)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.on(qProduct.categoryId.eq(QCategory.category.id))).thenReturn(sqlQueryMock);
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
            verify(sqlQueryMock, times(1)).where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse);
            verify(sqlQueryMock, times(1)).join(QCategory.category);
            verify(sqlQueryMock, times(1)).on(qProduct.categoryId.eq(QCategory.category.id));
            verify(sqlQueryMock, times(1)).orderBy(qProduct.id.desc());
            verify(sqlQueryMock, times(1)).offset(0L);
            verify(sqlQueryMock, times(1)).limit(10L);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }
        @Test
        void noConditions() {
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;

            // Arrange
            Predicate containsName = null;
            Predicate goePrice = null;
            Predicate loePrice = null;
            Predicate eqCategoryId = null;

            // Ensure that where method is stubbed with specific arguments
            when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.join(QCategory.category)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.on(qProduct.categoryId.eq(QCategory.category.id))).thenReturn(sqlQueryMock);
            when(sqlQueryMock.orderBy(qProduct.id.desc())).thenReturn(sqlQueryMock);
            when(sqlQueryMock.offset(anyLong())).thenReturn(sqlQueryMock);
            when(sqlQueryMock.limit(anyLong())).thenReturn(sqlQueryMock);

            // Act
            Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductConditions(
                    null, null, null, null, 0, 10);
            SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

            // Assert
            verify(sqlQueryMock, times(1)).select(qProduct);
            verify(sqlQueryMock, times(1)).from(qProduct);
            verify(sqlQueryMock, times(1)).where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse);
            verify(sqlQueryMock, times(1)).join(QCategory.category);
            verify(sqlQueryMock, times(1)).on(qProduct.categoryId.eq(QCategory.category.id));
            verify(sqlQueryMock, times(1)).orderBy(qProduct.id.desc());
            verify(sqlQueryMock, times(1)).offset(0L);
            verify(sqlQueryMock, times(1)).limit(10L);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }
        @Test
        void onlyName(){
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;

            // Arrange
            Predicate containsName = qProduct.name.containsIgnoreCase("Test");
            Predicate goePrice = null;
            Predicate loePrice = null;
            Predicate eqCategoryId = null;


            // Ensure that where method is stubbed with specific arguments
            when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.join(QCategory.category)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.on(qProduct.categoryId.eq(QCategory.category.id))).thenReturn(sqlQueryMock);
            when(sqlQueryMock.orderBy(qProduct.id.desc())).thenReturn(sqlQueryMock);
            when(sqlQueryMock.offset(anyLong())).thenReturn(sqlQueryMock);
            when(sqlQueryMock.limit(anyLong())).thenReturn(sqlQueryMock);

            // Act
            Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductConditions(
                    "Test", null, null, null, 0, 10);
            SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

            // Assert
            verify(sqlQueryMock, times(1)).select(qProduct);
            verify(sqlQueryMock, times(1)).from(qProduct);
            verify(sqlQueryMock, times(1)).where(containsName, goePrice, loePrice, eqCategoryId, deletedFalse);
            verify(sqlQueryMock, times(1)).join(QCategory.category);
            verify(sqlQueryMock, times(1)).on(qProduct.categoryId.eq(QCategory.category.id));
            verify(sqlQueryMock, times(1)).orderBy(qProduct.id.desc());
            verify(sqlQueryMock, times(1)).offset(0L);
            verify(sqlQueryMock, times(1)).limit(10L);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }

    }

    @Nested
    class GetSqlQueryProductById{
        @Test
        void success() {
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;

            // Arrange
            Predicate eqId = qProduct.id.eq(1L);

            // Ensure that where method is stubbed with specific arguments
            when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.where(eqId, deletedFalse)).thenReturn(sqlQueryMock);

            // Act
            Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductById(1L);
            SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

            // Assert
            verify(sqlQueryMock, times(1)).select(qProduct);
            verify(sqlQueryMock, times(1)).from(qProduct);
            verify(sqlQueryMock, times(1)).where(eqId, deletedFalse);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }
        @Test
        void failNotFoundDelete(){
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;
            product.delete();
            // Arrange
            Predicate eqId = qProduct.id.eq(1L);

            // Ensure that where method is stubbed with specific arguments
            when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.where(eqId, deletedFalse)).thenReturn(sqlQueryMock);

            // Act
            Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductById(1L);
            SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

            // Assert
            verify(sqlQueryMock, times(1)).select(qProduct);
            verify(sqlQueryMock, times(1)).from(qProduct);
            verify(sqlQueryMock, times(1)).where(eqId, deletedFalse);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }
        @Test
        void failNotFoundId(){
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;

            // Arrange
            Predicate eqId = qProduct.id.eq(2L);

            // Ensure that where method is stubbed with specific arguments
            when(sqlQueryMock.select(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.from(qProduct)).thenReturn(sqlQueryMock);
            when(sqlQueryMock.where(eqId, deletedFalse)).thenReturn(sqlQueryMock);

            // Act
            Function<SQLQuery<?>, SQLQuery<Product>> function = productService.getSqlQueryProductById(2L);
            SQLQuery<Product> resultQuery = function.apply(sqlQueryMock);

            // Assert
            verify(sqlQueryMock, times(1)).select(qProduct);
            verify(sqlQueryMock, times(1)).from(qProduct);
            verify(sqlQueryMock, times(1)).where(eqId, deletedFalse);

            // Verify the function returned the expected query
            assertSame(sqlQueryMock, resultQuery);
        }
    }

    @Nested
    class AddProduct{
        @Test
        void success() {
            Long categoryId = 1L;
            ProductInput input = new ProductInput("New Product", "New Description", 20, categoryId, 1500, "new_image.png");
            when(categoryRepository.findById(categoryId)).thenReturn(Mono.just(category));
            when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

            Mono<Product> savedProduct = productService.addProduct(input);

            StepVerifier.create(savedProduct)
                    .expectNext(product)
                    .verifyComplete();

            verify(categoryRepository, times(1)).findById(categoryId);
            verify(productRepository, times(1)).save(any(Product.class));
        }
        @Test
        void fail_CategoryNotFound() {
            long categoryId = 1L;
            ProductInput input = new ProductInput("New Product", "New Description", 20, categoryId, 1500, "new_image.png");
            when(categoryRepository.findById(categoryId)).thenReturn(Mono.empty());


            StepVerifier.create(productService.addProduct(input))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(categoryRepository, times(1)).findById(categoryId);
            verify(productRepository, times(0)).save(any(Product.class));
        }

    }

    @Nested
    class UpdateProduct{
        @Test
        void success() {
            ProductUpdateInput input = new ProductUpdateInput(1L, "Updated Product", "Updated Description", 20L, 1L, 2000L, "updated_image.png", false);
            when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

            Mono<Product> updatedProduct = productService.updateProduct(input);

            StepVerifier.create(updatedProduct)
                    .expectNext(product)
                    .verifyComplete();

            verify(productRepository, times(1)).save(any(Product.class));
        }
    }

    @Nested
    class DeleteProductById{
        @Test
        void success() {
            Long id = 1L;
            when(productRepository.findById(id)).thenReturn(Mono.just(product));
            when(productRepository.save(product)).thenReturn(Mono.empty());

            Mono<Void> result = productService.deleteProductById(id);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(productRepository, times(1)).findById(id);
            verify(productRepository, times(1)).save(product);
            assertTrue(product.isDeleted());
        }
        @Test
        void fail_NotFound() {
            Long id = 1L;
            when(productRepository.findById(id)).thenReturn(Mono.empty());

            Mono<Void> result = productService.deleteProductById(id);

            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(productRepository, times(1)).findById(id);
            verify(productRepository, times(0)).delete(product);
        }
    }

    @Nested
    class GetProductById{
        @Test
        void success() {
            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.one()).thenReturn(Mono.just(product));

            Mono<Product> foundProduct = productService.getProductById(1L);

            StepVerifier.create(foundProduct)
                    .expectNext(product)
                    .verifyComplete();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }

        @Test
        void failNotFoundId() {
            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.one()).thenReturn(Mono.empty());

            Mono<Product> foundProduct = productService.getProductById(2L);

            StepVerifier.create(foundProduct)
                    .verifyComplete();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }
        @Test
        void failNotFoundDelete() {
            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            product.delete();
            when(rowsFetchSpec.one()).thenReturn(Mono.just(product));

            Mono<Product> foundProduct = productService.getProductById(1L);

            StepVerifier.create(foundProduct)
                    .verifyComplete();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }

    }

    @Nested
    class TestBooleanExpressions{
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

}
