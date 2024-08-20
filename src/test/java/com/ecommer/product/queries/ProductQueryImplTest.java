package com.ecommer.product.queries;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QCategory;
import com.ecommer.product.entity.QProduct;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ProductQueryImplTest {
    Category category;
    Product product;
    private final BooleanExpression deletedFalse = QProduct.product.deleted.eq(false);

    private final ProductQueryImpl productQuery = new ProductQueryImpl();

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
    class GetSqlQueryProductConditions{
        @Test
        void allConditions() {
            // Mock SQLQuery and its methods
            SQLQuery<Product> sqlQueryMock = mock(SQLQuery.class);
            QProduct qProduct = QProduct.product;

            // Arrange
            Predicate containsName = qProduct.name.containsIgnoreCase("Test");
            Predicate goePrice = qProduct.price.goe(1500);
            Predicate loePrice = qProduct.price.loe(500);
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductConditions(
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductConditions(
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductConditions(
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductById(1L);
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductById(1L);
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
            Function<SQLQuery<?>, SQLQuery<Product>> function = productQuery.getSqlQueryProductById(2L);
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
    class TestBooleanExpressions{
        @Test
        void testLoePrice() {
            BooleanExpression booleanExpression = productQuery.loePrice(1000);
            assertNotNull(booleanExpression);
        }
        @Test
        void testGoePrice() {
            BooleanExpression booleanExpression = productQuery.goePrice(1000);
            assertNotNull(booleanExpression);
        }
        @Test
        void testContainsName() {
            BooleanExpression booleanExpression = productQuery.containsName("Test");
            assertNotNull(booleanExpression);
        }
        @Test
        void testContainsName_Null() {
            BooleanExpression booleanExpression = productQuery.containsName(null);
            assertNull(booleanExpression);
        }
        @Test
        void testLoePrice_Null() {
            BooleanExpression booleanExpression = productQuery.loePrice(null);
            assertNull(booleanExpression);
        }
        @Test
        void testGoePrice_Null() {
            BooleanExpression booleanExpression = productQuery.goePrice(null);
            assertNull(booleanExpression);
        }
        @Test
        void testEqCategoryId() {
            BooleanExpression booleanExpression = productQuery.eqCategoryId(1);
            assertNotNull(booleanExpression);
        }
        @Test
        void testEqCategoryId_Null() {
            BooleanExpression booleanExpression = productQuery.eqCategoryId(null);
            assertNull(booleanExpression);
        }
    }

}