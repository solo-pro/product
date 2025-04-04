package com.ecommer.product.exception;

public class ProductNotFound extends IllegalArgumentException {
    public ProductNotFound(long message) {
        super("Product not found id : " + message);
    }
}
