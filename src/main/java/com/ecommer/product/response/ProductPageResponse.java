package com.ecommer.product.response;

import java.util.List;

public record ProductPageResponse(
        PageInfoResponse pageInfo,
        List<ProductResponse> products
) {
}
