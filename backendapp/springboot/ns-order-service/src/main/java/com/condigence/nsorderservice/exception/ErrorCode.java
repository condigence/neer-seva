package com.condigence.nsorderservice.exception;

/**
 * Standard error codes for the Order Service.
 *
 * Prefix conventions:
 *   BUS-xxxx  - Business rule violations (validation, insufficient stock, etc.)
 *   TECH-xxxx - Technical failures (integration, DB, unexpected errors, etc.)
 */
public enum ErrorCode {

    // --- Business errors ---
    BUS_INVALID_REQUEST("BUS-0001"),
    BUS_SHOP_NOT_FOUND("BUS-0002"),
    BUS_STOCK_NOT_FOUND("BUS-0003"),
    BUS_INSUFFICIENT_STOCK("BUS-0004"),
    BUS_ORDER_NOT_FOUND("BUS-0005"),
    BUS_ITEM_NOT_FOUND("BUS-0006"),

    // --- Technical errors ---
    TECH_INTEGRATION_ERROR("TECH-1001"),
    TECH_DB_ERROR("TECH-1002"),
    TECH_UNEXPECTED_ERROR("TECH-1999");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
