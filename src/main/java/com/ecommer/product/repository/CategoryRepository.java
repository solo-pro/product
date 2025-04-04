package com.ecommer.product.repository;

import com.ecommer.product.entity.Category;
import com.ecommer.product.queries.CategoryQuery;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;

public interface CategoryRepository extends QuerydslR2dbcRepository<Category, Long>, CategoryQuery {
}
