package com.ecommer.product.service;

import com.ecommer.product.codegen.types.ProductInput;
import com.ecommer.product.codegen.types.ProductUpdateInput;
import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QProduct;
import com.ecommer.product.exception.ProductNotFound;
import com.ecommer.product.queries.ProductQuery;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductQuery productQuery;

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
                                    response.category().name().equals("Electronics")
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
                                    response.category().name().equals("Electronics")
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
                                    response.category().name().equals("Electronics")
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
    class AddProduct{
        @Test
        void success() {
            Long categoryId = 1L;
            ProductInput input = new ProductInput("New Product", 1500, 20, "new_image.png", "New Description",categoryId.toString());
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
            Long categoryId = 1L;
            ProductInput input = new ProductInput("New Product", 1500, 20, "new_image.png", "New Description",categoryId.toString());
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
            ProductUpdateInput input = new ProductUpdateInput("1", "Updated Product", 2000, 20, "updated_image.png", "Updated Description", "1", false);
            when(productRepository.findById(Long.parseLong(input.getId()))).thenReturn(Mono.just(product));
            when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
            Mono<Product> updatedProduct = productService.updateProduct(input);

            StepVerifier.create(updatedProduct)
                    .expectNext(product)
                    .verifyComplete();

            verify(productRepository, times(1)).save(any(Product.class));
            assertEquals("Updated Product", product.getName());
        }
        @Test
        void failNotFound(){
            ProductUpdateInput input = new ProductUpdateInput("1", "Updated Product", 2000, 20, "updated_image.png", "Updated Description", "1", false);
            when(productRepository.findById(Long.parseLong(input.getId()))).thenReturn(Mono.empty());
            Mono<Product> updatedProduct = productService.updateProduct(input);

            StepVerifier.create(updatedProduct)
                    .expectError(ProductNotFound.class)
                    .verify();

            verify(productRepository, times(0)).save(any(Product.class));

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
        void failNotFound() {
            Long id = 1L;
            when(productRepository.findById(id)).thenReturn(Mono.empty());

            Mono<Void> result = productService.deleteProductById(id);

            StepVerifier.create(result)
                    .expectError(ProductNotFound.class)
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

            Mono<ProductResponse> foundProduct = productService.getProductById(1L);

            StepVerifier.create(foundProduct)
                    .expectNext(ProductResponse.from(product, category))
                    .verifyComplete();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }

        @Test
        void failNotFoundId() {
            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            when(rowsFetchSpec.one()).thenReturn(Mono.empty());


            StepVerifier.create(productService.getProductById(2L))
                    .expectError(ProductNotFound.class)
                    .verify();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }
        @Test
        void failNotFoundDelete() {
            RowsFetchSpec<Object> rowsFetchSpec = mock(RowsFetchSpec.class);
            when(productRepository.query(any())).thenReturn(rowsFetchSpec);
            product.delete();
            when(rowsFetchSpec.one()).thenReturn(Mono.empty());

            StepVerifier.create(productService.getProductById(2L))
                    .expectError(ProductNotFound.class)
                    .verify();

            verify(productRepository, times(1)).query(any());
            verify(rowsFetchSpec, times(1)).one();
        }

    }



}
