package com.ecommer.product.response;

public record PageInfoResponse(
        long totalItems,
        long totalPages,
        long currentPage,
        boolean hasNextPage
) {

}
