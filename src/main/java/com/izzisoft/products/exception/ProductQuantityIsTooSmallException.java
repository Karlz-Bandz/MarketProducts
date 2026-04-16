package com.izzisoft.products.exception;

public class ProductQuantityIsTooSmallException extends RuntimeException {
    public ProductQuantityIsTooSmallException(String message) {
        super(message);
    }
}
