package com.condigence.stockservice.exception;

/**
 * Thrown when a Shop or related resource (e.g. vendor's shop list) cannot be found.
 */
public class ShopNotFoundException extends RuntimeException {

    public ShopNotFoundException(String message) {
        super(message);
    }
}
