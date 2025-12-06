package com.condigence.imageservice.exception;

/**
 * Standard error codes used by the Image Service.
 *
 * BUSINESS_* codes are for domain or validation failures.
 * TECHNICAL_* codes are for infrastructure or unexpected system errors.
 */
public enum ErrorCode {

    // Business errors
    BUSINESS_IMAGE_NOT_FOUND,
    BUSINESS_IMAGE_NAME_NOT_FOUND,
    BUSINESS_INVALID_IMAGE_REQUEST,

    // Technical errors
    TECHNICAL_STORAGE_FAILURE,
    TECHNICAL_DATABASE_ERROR,
    TECHNICAL_EXTERNAL_SERVICE_ERROR,
    TECHNICAL_UNEXPECTED_ERROR
}

