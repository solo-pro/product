package com.ecommer.product.controller;


import com.ecommer.product.entity.Product;
import com.ecommer.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    @QueryMapping
    public Flux<Product> products(
            @Argument String name,
            @Argument Long category,
            @Argument Long overprice,
            @Argument Long underprice,
            @Argument Integer page,
            @Argument Integer limit
    ) {


        return null;
    }
}


