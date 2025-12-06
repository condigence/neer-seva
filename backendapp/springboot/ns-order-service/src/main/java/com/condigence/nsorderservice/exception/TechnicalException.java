package com.condigence.nsorderservice.exception;

/**
 * TechnicalException represents non-business failures (integration, infra, etc.).
 * These typically map to 5xx HTTP statuses.
 */
public class TechnicalException extends RuntimeException {

    private final ErrorCode errorCode;

    public TechnicalException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

