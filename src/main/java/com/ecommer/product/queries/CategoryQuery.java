package com.ecommer.product.queries;

import com.ecommer.product.entity.Category;
import com.querydsl.sql.SQLQuery;

import java.util.function.Function;

public interface CategoryQuery {
    Function<SQLQuery<?>, SQLQuery<Category>> getSqlQueryCategoryConditions(long page, Integer size);
}
