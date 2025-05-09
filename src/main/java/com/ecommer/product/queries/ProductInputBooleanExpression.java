package com.ecommer.product.queries;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface ProductInputBooleanExpression {
    BooleanExpression eqCategoryId(Integer categoryId);
    BooleanExpression loePrice(Integer underprice);
    BooleanExpression goePrice(Integer overprice);
    BooleanExpression containsName(String name);
}
