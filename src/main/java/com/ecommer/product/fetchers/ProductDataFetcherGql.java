package com.ecommer.product.fetchers;

import com.ecommer.product.arguments.ProductInput;
import com.ecommer.product.arguments.ProductUpdateInput;
import com.ecommer.product.entity.Product;
import com.ecommer.product.response.ProductResponse;
import com.ecommer.product.service.ProductService;
import com.netflix.graphql.dgs.*;
import graphql.language.Field;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@DgsComponent
@RequiredArgsConstructor
public class ProductDataFetcherGql {
    private final ProductService productService;


    @DgsQuery
    public Flux<ProductResponse> products(
            @InputArgument String name
            , @InputArgument Integer overprice
            , @InputArgument Integer underprice
            , @InputArgument Integer categoryId
            , @InputArgument Integer page
            , @InputArgument Integer size
            , DgsDataFetchingEnvironment dataFetchingEnvironment
    ) {
        var category = dataFetchingEnvironment.getField()
                .getSelectionSet()
                .getSelections()
                .stream()
                .filter(selection -> {
                    Field field = (Field) selection;
                    return field.getName().equals("category");
                }).findFirst();

        if (category.isEmpty()){
            return productService.getProductsByFilters(name, overprice, underprice, categoryId, page, size);
        }
        return productService.getProductsAndCategoryByFilters(name, overprice, underprice, categoryId, page, size);
    }


    @DgsQuery
    public Mono<Product> product(@InputArgument String id) {
        return productService.getProductById(Long.parseLong(id));
    }

    @DgsMutation
    public Mono<Product> addProduct(@InputArgument ProductInput input) {
        return productService.addProduct(input);
    }

    @DgsMutation
    public Mono<Product> updateProduct(@InputArgument ProductUpdateInput input) {
        return productService.updateProduct(input);
    }

    @DgsMutation
    public Mono<Void> deleteProduct(@InputArgument String id) {
        return productService.deleteProductById(Long.parseLong(id));
    }

}
