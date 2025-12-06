package com.condigence.nsorderservice.exception;

/**
 * BusinessException represents domain/business rule violations.
 * These map to 4xx HTTP statuses (typically 400/404).
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

