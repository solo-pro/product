package com.ecommer.product.fetchers;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.CategoryResponse;
import com.ecommer.product.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    List<Product> products;
    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Category category = Category.builder().name("category" + i).description("category description " + i).build();
            categories.add(category);
        }
        Flux<Category> categoryFlux = categoryRepository.saveAll(categories);
        categoryFlux.collectList().block();

        products = new ArrayList<>();
        for (int i = 100; i >= 1; i--) {
            Product product = Product.builder().name("product" + i).description("product description " + i).price((long) i*1000).stock(i).mainImage("main image " + i).categoryId((long) i % 3 + 1).build();
            products.add(product);
        }
        Flux<Product> productFlux = productRepository.saveAll(products);
        productFlux.collectList().block();
    }
    @AfterEach
    void tearDown() {
        productRepository.deleteAll().block();
    }

    @Nested
    class Products {
        @Test
        void allCondition() {
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(name: \"product\", overprice: 100000000, underprice: 1, categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps category { id name description } } }");

            ProductResponse[] productResponses = getProductResponses(execute);
            assertEquals(10, productResponses.length);
            assertNotNull(productResponses[0].id());
            assertEquals("product3", productResponses[0].name());
            assertEquals("product description 3", productResponses[0].description());
            assertEquals(3000, productResponses[0].price());
            assertEquals(3, productResponses[0].stock());
            assertEquals("main image 3", productResponses[0].mainImage());
            CategoryResponse category = productResponses[0].category();
            assertNotNull(category);
            assertEquals(1, category.id());
            assertEquals("category1", category.name());
            assertEquals("category description 1", category.description());
        }



        @Test
        void allConditionNotIncludeCategory(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(name: \"product\", overprice: 100000000, underprice: 1, categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps } }");
            ProductResponse[] productResponses = getProductResponses(execute);

            assertNotNull(productResponses);
            assertEquals(10, productResponses.length);
            assertNotNull(productResponses[0].id());
            assertEquals("product3", productResponses[0].name());
            assertEquals("product description 3", productResponses[0].description());
            assertEquals(3000, productResponses[0].price());
            assertEquals(3, productResponses[0].stock());
            assertEquals("main image 3", productResponses[0].mainImage());
            assertNull(productResponses[0].updatedTimeStamps());

            CategoryResponse category = productResponses[0].category();
            assertNull(category);

        }
        @Test
        void overpriceCondition(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(overprice: 1000, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps category { id name description } } }");
            ProductResponse[] productResponses = getProductResponses(execute);
            assertNotNull(productResponses);
            assertEquals(1, productResponses.length);
            assertNotNull(productResponses[0].id());
            assertEquals("product1", productResponses[0].name());
            assertEquals("product description 1", productResponses[0].description());
            assertEquals(1000, productResponses[0].price());
            CategoryResponse category = productResponses[0].category();
            assertNotNull(category);
            assertEquals(2, category.id());

        }

        @Test
        void underpriceCondition(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(underprice: 99999, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            ProductResponse[] productResponses = getProductResponses(execute);
            assertNotNull(productResponses);
            assertEquals(1, productResponses.length);
            assertNotNull(productResponses[0].id());
            assertEquals("product100", productResponses[0].name());
            assertEquals("product description 100", productResponses[0].description());
            assertEquals(100000, productResponses[0].price());
        }

        @Test
        void underpriceAndOverpriceCondition(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(underprice: 1000, overprice: 5000, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            ProductResponse[] productResponses = getProductResponses(execute);
            assertNotNull(productResponses);
            assertEquals(5, productResponses.length);
        }

        @Test
        void categoryIdCondition(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            ProductResponse[] productResponses = getProductResponses(execute);
            assertNotNull(productResponses);
            assertEquals(10, productResponses.length);

        }

        @Test
        void categoryIdWithPageCondition(){
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ products(categoryId: 1, page: 3, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            ProductResponse[] productResponses = getProductResponses(execute);
            assertNotNull(productResponses);
            assertEquals(3, productResponses.length);

        }

        private ProductResponse[] getProductResponses(ExecutionResult execute) {
            List<GraphQLError> errors = execute.getErrors();
            assertEquals(new ArrayList<>(), errors);
            LinkedHashMap<String, Object> data = execute.getData();
            assertNotNull(data);
            assertNotNull(data.get("products"));
            return objectMapper.convertValue(data.get("products"), ProductResponse[].class);
        }
    }
    @Nested
    class getById{
        @Test
        void success(){
            Long id = products.get(0).getId();
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ product(id: \"" + id + "\") { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");

            LinkedHashMap<String, Object> data = execute.getData();
            ProductResponse product = objectMapper.convertValue(data.get("product"), ProductResponse.class);

            assertNotNull(data);
            assertEquals(id, product.id());
            assertEquals("product100", product.name());
            assertEquals("product description 100", product.description());
            assertEquals(100000, product.price());
            assertEquals(100, product.stock());
            assertEquals("main image 100", product.mainImage());

            List<GraphQLError> errors = execute.getErrors();
            assertEquals(new ArrayList<>(), errors);
        }
        @Test
        void notFound(){
            long id = -9999;
            ExecutionResult execute = dgsQueryExecutor
                    .execute("{ product(id: \"" + id + "\") { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            LinkedHashMap<String, Object> data = execute.getData();
            ProductResponse product = objectMapper.convertValue(data.get("product"), ProductResponse.class);
            assertNull(product);
            List<GraphQLError> errors = execute.getErrors();
            assertEquals(1, errors.size());
            assertEquals("Product not found id : -9999", errors.get(0).getMessage());
        }
        @Test
        void invalidId(){
            String id = "invalid";
            ExecutionResult execute = dgsQueryExecutor.execute("{ product(id: \"" + id + "\") { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }");
            LinkedHashMap<String, Object> data = execute.getData();
            ProductResponse product = objectMapper.convertValue(data.get("product"), ProductResponse.class);
            assertNull(product);
            List<GraphQLError> errors = execute.getErrors();
            assertEquals(1, errors.size());
            assertEquals("For input format invalid", errors.get(0).getMessage());
        }
    }

    @Nested
    class AddProduct{
        @Test
        void addProduct(){
            Map<String, Object> map = Map.of("name", "new product", "description", "new product description", "price", 1000, "stock", 10, "mainImage", "new main image", "categoryId", 1);
            ExecutionResult execute = dgsQueryExecutor
                    .execute("mutation addProduct($input: ProductInput!){ addProduct(input: {" +
                                    "name:$name, " +
                                    "price:$price, " +
                                    "stock: $stock, " +
                                    "categoryId: $categoryId, " +
                                    "mainImage: $mainImage, " +
                                    "description: $description" +
                                    "}) " +
                                    "{ id name description price stock mainImage createdTimeStamps updatedTimeStamps } " +
                                    "}"
                    ,map, "");
            System.out.println(execute);
            LinkedHashMap<String, Object> data = execute.getData();
            ProductResponse productResponse = objectMapper.convertValue(data.get("addProduct"), ProductResponse.class);
            assertNotNull(productResponse);
            assertNotNull(productResponse.id());
            assertEquals("new product", productResponse.name());
            assertEquals("new product description", productResponse.description());
            assertEquals(1000, productResponse.price());
            assertEquals(10, productResponse.stock());
            assertEquals("new main image", productResponse.mainImage());
            assertNotNull(productResponse.createdTimeStamps());
            assertNull(productResponse.updatedTimeStamps());
            List<GraphQLError> errors = execute.getErrors();
            assertEquals(new ArrayList<>(), errors);
        }
    }
    @Test
    void addProduct() {

    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}