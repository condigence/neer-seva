package com.condigence.nsproductservice.exception;

import com.condigence.nsproductservice.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Business-level exception representing domain rule violations or expected error conditions.
 */
public class BusinessException extends AbstractApiException {

    public BusinessException(String message, ErrorCode errorCode) {
        super(message, errorCode, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
