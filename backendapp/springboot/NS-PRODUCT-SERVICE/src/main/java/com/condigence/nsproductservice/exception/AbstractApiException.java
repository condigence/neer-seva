package com.condigence.nsproductservice.exception;

import com.condigence.nsproductservice.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Base runtime exception for all API errors, holding an ErrorCode and HttpStatus.
 */
public abstract class AbstractApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    protected AbstractApiException(String message, Throwable cause, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    protected AbstractApiException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        this(message, null, errorCode, httpStatus);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

