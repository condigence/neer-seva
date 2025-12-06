package com.condigence.nsuserservice.exception;

public class TechnicalException extends RuntimeException {

    private final ErrorCode errorCode;

    public TechnicalException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TechnicalException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
