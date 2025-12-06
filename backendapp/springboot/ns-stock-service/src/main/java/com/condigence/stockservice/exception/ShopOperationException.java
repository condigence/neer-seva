package com.condigence.stockservice.exception;

/**
 * Generic business exception for shop-related operations that fail for domain reasons
 * other than simple "not found".
 */
public class ShopOperationException extends RuntimeException {

    public ShopOperationException(String message) {
        super(message);
    }
}

