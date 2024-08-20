package com.ecommer.product.queries;

import com.ecommer.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.ecommer.product.entity.QCategory.category;
import static com.ecommer.product.entity.QProduct.product;

@Component
public class ProductQueryImpl implements ProductQuery, ProductInputBooleanExpression {
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
    public Function<SQLQuery<?>, SQLQuery<Product>> getSqlQueryProductById(Long id) {
        return sqlQuery -> sqlQuery.select(product)
                .from(product)
                .where(product.id.eq(id), product.deleted.eq(false));
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
