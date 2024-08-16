package com.ecommer.product.response;

public record PageInfoResponse(
        long totalItems,
        long totalPages,
        long currentPage,
        boolean hasNextPage
) {
    public static PageInfoResponse from(long totalItems, long totalPages, long currentPage, boolean hasNextPage) {
        return new PageInfoResponse(totalItems, totalPages, currentPage, hasNextPage);
    }
}
