package com.ecommer.product.queries;

import com.ecommer.product.entity.Product;
import com.querydsl.sql.SQLQuery;

import java.util.function.Function;

public interface ProductQuery {
    Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductConditions(String name, Integer overprice, Integer underprice, Integer categoryId, long page, Integer size);
    Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductById(Long id);
}
