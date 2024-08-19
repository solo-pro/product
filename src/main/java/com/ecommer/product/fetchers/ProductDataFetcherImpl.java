package com.ecommer.product.fetchers;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QCategory;
import com.ecommer.product.entity.QProduct;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
import com.ecommer.product.service.ProductInputBooleanExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import graphql.language.Field;
import graphql.language.Selection;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductDataFetcherImpl
//        implements RuntimeWiringConfigurer, ProductInputBooleanExpression, ProductDataFetcher
        {
//    private final ProductRepository productRepository;
//    private final CategoryRepository categoryRepository;
//    private static final QProduct product = QProduct.product;
//    private static final QCategory category = QCategory.category;
//
//
//    @Override
//    public void configure(RuntimeWiring.Builder builder) {
//        builder.type("Query", typeWiring -> typeWiring
//                .dataFetcher("products", getFetcherProducts())
//                .dataFetcher("product", getFetcherProductById())
//        ).build();
//    }
//
//    @Override
//    public DataFetcher<Flux<ProductResponse>> getFetcherProducts() {
//        return env -> {
//            String name = env.getArgument("name");
//            Integer overPrice = env.getArgument("overPrice");
//            Integer underPrice = env.getArgument("underPrice");
//            Long categoryId = env.getArgument("categoryId");
//            Integer page = env.getArgument("page");
//            Integer size = env.getArgument("size");
//            Field field = env.getField();
//            Optional<?> cate = field
//                    .getSelectionSet()
//                    .getSelections()
//                    .stream()
//                    .filter(selection -> selection.getAdditionalData().containsKey("category"))
//                    .findFirst();
//            if(cate.isEmpty()) return getAllProductFetcher(name, overPrice, underPrice, categoryId, page, size)
//                    .map(prod -> ProductResponse.from(prod, null));
//            Mono<Map<Long, Category>> mapMono = categoryRepository.findAll()
//                    .collectMap(Category::getId);
//            return mapMono.flatMapMany(categoryMap ->
//                    getAllProductFetcher(name, overPrice, underPrice, categoryId, page, size)
//                            .map(prod -> {
//                                Category getCategory = categoryMap.get(prod.getCategoryId());
//                                return ProductResponse.from(prod, getCategory);
//                            })
//            );
//        };
//    }
//
//    @Override
//    public DataFetcher<Mono<Product>> getFetcherProductById() {
//        return env -> {
//            String idStr = env.getArgument("id");
//            assert idStr != null;
//            Long id = Long.parseLong(idStr);
//            return productRepository.query(
//                    sqlQuery -> sqlQuery.select(product).from(product).where(product.id.eq(id))
//            ).one();
//        };
//    }
//
//    private Flux<Product> getAllProductFetcher(String name, Integer overPrice, Integer underPrice, Long categoryId, Integer page, Integer size) {
//        return productRepository.query(sqlQuery ->
//                sqlQuery.select(product)
//                        .from(product)
//                        .join(category)
//                        .on(category.id.eq(product.categoryId))
//                        .where(
//                                expectName(name),
//                                expectPrice(overPrice, underPrice),
//                                expectCategoryId(categoryId)
//                        )
//                        .orderBy(product.createdTimeStamps.desc())
//                        .offset((long) page * size).limit(size)
//        ).all();
//
//    }
//
//
//    @Override
//    public BooleanExpression expectName(String name) {
//        if (name != null) {
//            return product.name.contains(name);
//        }
//        return null;
//    }
//
//    @Override
//    public BooleanExpression expectPrice(Integer overPrice, Integer underPrice) {
//        if (overPrice != null && underPrice != null) {
//            return product.price.between(overPrice, underPrice);
//        }
//        if (overPrice != null) {
//            return product.price.goe(overPrice);
//        }
//        if (underPrice != null) {
//            return product.price.loe(underPrice);
//        }
//        return null;
//    }
//
//    @Override
//    public BooleanExpression expectCategoryId(Long categoryId) {
//        if (categoryId != null) {
//            return product.categoryId.eq(categoryId);
//        }
//        return null;
//    }


}
