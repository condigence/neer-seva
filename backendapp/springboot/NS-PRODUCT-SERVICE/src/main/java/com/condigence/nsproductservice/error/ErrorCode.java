package com.condigence.nsproductservice.error;

/**
 * Application-wide standard error codes for both business and technical errors.
 * Codes are stable and are intended to be relied upon by API clients.
 */
public enum ErrorCode {

    // Generic / technical
    INTERNAL_ERROR("INTERNAL_ERROR", "Unexpected internal server error"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Request validation failed"),
    INTEGRATION_FAILURE("INTEGRATION_FAILURE", "Downstream service call failed"),

    // Generic resource / request
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Requested resource was not found"),
    BAD_REQUEST("BAD_REQUEST", "Bad request"),

    // Domain: Brand
    BRAND_NOT_FOUND("BRAND_NOT_FOUND", "Brand not found"),

    // Domain: Item
    ITEM_NOT_FOUND("ITEM_NOT_FOUND", "Item not found");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

