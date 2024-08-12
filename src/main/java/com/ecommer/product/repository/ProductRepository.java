package com.ecommer.product.repository;

import com.ecommer.product.entity.Product;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;


public interface ProductRepository
        extends QuerydslR2dbcRepository<Product,Long> {
}
