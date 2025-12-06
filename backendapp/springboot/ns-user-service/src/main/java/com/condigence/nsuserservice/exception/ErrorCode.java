package com.condigence.nsuserservice.exception;

public enum ErrorCode {

    // Business errors (4xx)
    USER_ALREADY_REGISTERED("USR-001"),
    USER_NOT_FOUND("USR-002"),
    CONTACT_MISSING("USR-003"),
    INVALID_OTP("USR-004"),
    ADDRESS_NOT_FOUND("ADR-001"),

    // Technical errors (5xx)
    INTERNAL_ERROR("GEN-001"),
    DOWNSTREAM_SERVICE_ERROR("GEN-002");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
