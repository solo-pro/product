package com.ecommer.product.response;



import java.util.ArrayList;
import java.util.List;

public record ProductPageResponse(
        PageInfoResponse pageInfo,
        List<ProductResponse> products
) {
    public static ProductPageResponse from(PageInfoResponse pageInfo) {
        return new ProductPageResponse(pageInfo, new ArrayList<>());
    }
}
