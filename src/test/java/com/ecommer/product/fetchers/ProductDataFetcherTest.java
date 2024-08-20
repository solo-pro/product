package com.ecommer.product.fetchers;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;



    @BeforeEach
    void setUp() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Category category = Category.builder().name("category" + i).description("category description " + i).build();
            categories.add(category);
        }
        Flux<Category> categoryFlux = categoryRepository.saveAll(categories);
        categoryFlux.collectList().block();

        List<Product> products = new ArrayList<>();
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
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(name: \"product\", overprice: 100000000, underprice: 1, categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps category { id name description } } }", "data.products");
            assertNotNull(data);
            assertEquals(10, data.size());
            assertNotNull(data.get(0).get("id"));
            assertEquals("product3", data.get(0).get("name"));
            assertEquals("product description 3", data.get(0).get("description"));
            assertEquals(3000, data.get(0).get("price"));
            assertEquals(3, data.get(0).get("stock"));
            assertEquals("main image 3", data.get(0).get("mainImage"));
            Map<String, Object> category = (Map<String, Object>) data.get(0).get("category");
            assertNotNull(category);
            assertEquals("1", category.get("id"));
            assertEquals("category1", category.get("name"));
            assertEquals("category description 1", category.get("description"));
        }
        @Test
        void allConditionNotIncludeCategory(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(name: \"product\", overprice: 100000000, underprice: 1, categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }", "data.products");
            assertNotNull(data);
            assertEquals(10, data.size());
            assertNotNull(data.get(0).get("id"));
            assertEquals("product3", data.get(0).get("name"));
            assertEquals("product description 3", data.get(0).get("description"));
            assertEquals(3000, data.get(0).get("price"));
            assertEquals(3, data.get(0).get("stock"));
            assertEquals("main image 3", data.get(0).get("mainImage"));
            Map<String, Object> category = (Map<String, Object>) data.get(0).get("category");
            assertNull(category);
        }
        @Test
        void overpriceCondition(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(overprice: 1000, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps category { id name description } } }", "data.products");
            assertNotNull(data);
            assertEquals(1, data.size());
            assertNotNull(data.get(0).get("id"));
            assertEquals("product1", data.get(0).get("name"));
            assertEquals("product description 1", data.get(0).get("description"));
            assertEquals(1000, data.get(0).get("price"));
            assertEquals(1, data.get(0).get("stock"));
            assertEquals("main image 1", data.get(0).get("mainImage"));
            Map<String, Object> category = (Map<String, Object>) data.get(0).get("category");
            assertNotNull(category);
            assertEquals("2", category.get("id"));
            assertEquals("category2", category.get("name"));
            assertEquals("category description 2", category.get("description"));
        }

        @Test
        void underpriceCondition(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(underprice: 99999, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }", "data.products");
            assertNotNull(data);
            assertEquals(1, data.size());
            assertNotNull(data.get(0).get("id"));
            assertEquals("product100", data.get(0).get("name"));
            assertEquals("product description 100", data.get(0).get("description"));
            assertEquals(100000, data.get(0).get("price"));
            assertEquals(100, data.get(0).get("stock"));
            assertEquals("main image 100", data.get(0).get("mainImage"));
        }

        @Test
        void underpriceAndOverpriceCondition(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(underprice: 1000, overprice: 5000, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }", "data.products");
            assertNotNull(data);
            System.out.println(data);
            assertEquals(5, data.size());
        }

        @Test
        void categoryIdCondition(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(categoryId: 1, page: 0, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }", "data.products");
            assertNotNull(data);
            assertEquals(10, data.size());
        }

        @Test
        void categoryIdWithPageCondition(){
            List<LinkedHashMap<String, Object>> data = dgsQueryExecutor
                    .executeAndExtractJsonPath("{ products(categoryId: 1, page: 3, size: 10) { id name description price stock mainImage createdTimeStamps updatedTimeStamps } }", "data.products");
            assertNotNull(data);
            assertEquals(3, data.size());
        }
    }

    @Test
    void product() {
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