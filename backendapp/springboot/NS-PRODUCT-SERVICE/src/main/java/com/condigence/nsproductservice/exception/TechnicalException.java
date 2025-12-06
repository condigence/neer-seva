package com.condigence.nsproductservice.exception;

import com.condigence.nsproductservice.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Technical exception used for infrastructure or unexpected failures.
 */
public class TechnicalException extends AbstractApiException {

    public TechnicalException(String message, ErrorCode errorCode) {
        super(message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TechnicalException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TechnicalException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}

