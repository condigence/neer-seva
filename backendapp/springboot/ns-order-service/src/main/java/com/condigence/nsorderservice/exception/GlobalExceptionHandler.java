package com.condigence.nsorderservice.exception;

import com.condigence.nsorderservice.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("ResourceNotFoundException: {}", ex.getMessage());
        ErrorResponse body = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request,
                ErrorCode.BUS_ORDER_NOT_FOUND.getCode());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
        logger.warn("BadRequestException: {}", ex.getMessage());
        ErrorResponse body = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request,
                ErrorCode.BUS_INVALID_REQUEST.getCode());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        logger.warn("BusinessException: {}", ex.getMessage());
        ErrorCode code = ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCode.BUS_INVALID_REQUEST;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (code == ErrorCode.BUS_ORDER_NOT_FOUND || code == ErrorCode.BUS_SHOP_NOT_FOUND || code == ErrorCode.BUS_STOCK_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), request, code.getCode());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ErrorResponse> handleTechnicalException(TechnicalException ex, WebRequest request) {
        logger.error("TechnicalException: {}", ex.getMessage(), ex);
        ErrorCode code = ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCode.TECH_UNEXPECTED_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), request, code.getCode());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorResponse body = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request,
                ErrorCode.TECH_UNEXPECTED_ERROR.getCode());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, WebRequest request, String errorCode) {
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setMessage(message);
        body.setPath(request.getDescription(false));
        body.setErrorCode(errorCode);
        return body;
    }
}
