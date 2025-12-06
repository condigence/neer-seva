package com.condigence.nsproductservice.exception;

import com.condigence.nsproductservice.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Convenience exception for 404 resource-not-found scenarios.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }
}
