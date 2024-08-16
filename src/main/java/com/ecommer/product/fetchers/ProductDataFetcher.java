package com.ecommer.product.fetchers;

import com.ecommer.product.entity.Category;
import com.ecommer.product.entity.Product;
import com.ecommer.product.entity.QCategory;
import com.ecommer.product.entity.QProduct;
import com.ecommer.product.repository.CategoryRepository;
import com.ecommer.product.repository.ProductRepository;
import com.ecommer.product.response.ProductResponse;
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
public class ProductDataFetcher implements RuntimeWiringConfigurer {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final QProduct product = QProduct.product;
    private final QCategory category = QCategory.category;


    @Override
    public void configure(RuntimeWiring.Builder builder) {

        builder.type("Query", typeWiring -> typeWiring
                .dataFetcher("products", getFetcherProducts())
                .dataFetcher("product", getFetcherProductById())
        ).build();



    }

    private DataFetcher<Flux<ProductResponse>> getFetcherProducts() {
        return (env) -> {
            String name = env.getArgument("name");
            Integer overPrice = env.getArgument("overPrice");
            Integer underPrice = env.getArgument("underPrice");
            Long categoryId = env.getArgument("categoryId");
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            Field field = env.getField();
            Optional<Selection> cate = field
                    .getSelectionSet()
                    .getSelections()
                    .stream()
                    .filter(selection -> selection.getAdditionalData().containsKey("category"))
                    .findFirst();
            if(cate.isEmpty()) return getAllProductFetcher(name, overPrice, underPrice, categoryId, page, size)
                    .map(product -> ProductResponse.from(product, null));
            Mono<Map<Long, Category>> mapMono = categoryRepository.findAll()
                    .collectMap(Category::getId);
            return mapMono.flatMapMany(categoryMap ->
                    getAllProductFetcher(name, overPrice, underPrice, categoryId, page, size)
                            .map(product -> {
                                Category category = categoryMap.get(product.getCategoryId());
                                return ProductResponse.from(product, category);
                            })
            );
        };
    }


    private Flux<Product> getAllProductFetcher(String name, Integer overPrice, Integer underPrice, Long categoryId, Integer page, Integer size) {
        return productRepository.query(sqlQuery ->
                sqlQuery.select(product)
                        .from(product)
                        .join(category)
                        .on(category.id.eq(product.categoryId))
                        .where(
                                expectName(name),
                                expectPrice(overPrice, underPrice),
                                expectCategoryId(categoryId)
                        )
                        .orderBy(product.createdTimeStamps.desc())
                        .offset(page * size).limit(size)
        ).all();

    }



    private static BooleanExpression expectName(String name) {
        if (name != null) {
            return product.name.contains(name);
        }
        return null;
    }
    private static BooleanExpression expectPrice(Integer overPrice, Integer underPrice) {
        if (overPrice != null && underPrice != null) {
            return product.price.between(overPrice, underPrice);
        }
        if (overPrice != null) {
            return product.price.goe(overPrice);
        }
        if (underPrice != null) {
            return product.price.loe(underPrice);
        }
        return null;
    }
    private static BooleanExpression expectCategoryId(Long categoryId) {
        if (categoryId != null) {
            return product.categoryId.eq(categoryId);
        }
        return null;
    }

    private DataFetcher<Mono<Product>> getFetcherProductById() {
        return (env) -> {
            String idStr = env.getArgument("id");
            assert idStr != null;
            Long id = Long.parseLong(idStr);
            return productRepository.query(
                    sqlQuery -> sqlQuery.select(product).from(product).where(product.id.eq(id))
            ).one();
        };
    }
}
