package com.ecommer.product.queries;

import com.ecommer.product.entity.Category;
import com.querydsl.sql.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

import static com.ecommer.product.entity.QCategory.category;

@Repository
public class CategoryQueryImpl implements CategoryQuery{
    @Override
    public Function<SQLQuery<?>, SQLQuery<Category>> getSqlQueryCategoryConditions(long page, Integer size) {
        return query -> query
                .select(category)
                .from(category)
                .offset(page * size)
                .limit(size);
    }
}
