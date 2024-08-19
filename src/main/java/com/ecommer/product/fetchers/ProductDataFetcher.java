package com.ecommer.product.fetchers;

import com.ecommer.product.entity.Product;
import com.ecommer.product.response.ProductResponse;
import graphql.schema.DataFetcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDataFetcher {
    DataFetcher<Flux<ProductResponse>> getFetcherProducts();
    DataFetcher<Mono<Product>> getFetcherProductById();
}
