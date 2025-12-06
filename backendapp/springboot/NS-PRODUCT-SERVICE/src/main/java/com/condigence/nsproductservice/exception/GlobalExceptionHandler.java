package com.condigence.nsproductservice.exception;

import com.condigence.nsproductservice.error.ApiErrorResponse;
import com.condigence.nsproductservice.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global REST exception handler that maps business/technical exceptions to a standard error response.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        HttpStatus status = ex.getHttpStatus();
        ErrorCode code = ex.getErrorCode();
        ApiErrorResponse body = buildBody(status, code, ex.getMessage(), request.getRequestURI());
        log.warn("Business exception: code={}, path={}, message={}", code.getCode(), request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ApiErrorResponse> handleTechnicalException(TechnicalException ex, HttpServletRequest request) {
        HttpStatus status = ex.getHttpStatus();
        ErrorCode code = ex.getErrorCode();
        ApiErrorResponse body = buildBody(status, code, ex.getMessage(), request.getRequestURI());
        log.error("Technical exception: code={}, path={}", code.getCode(), request.getRequestURI(), ex);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse(code.getDefaultMessage());
        ApiErrorResponse body = buildBody(status, code, message, request.getRequestURI());
        log.warn("Validation error on path {}: {}", request.getRequestURI(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        ApiErrorResponse body = buildBody(status, code, code.getDefaultMessage(), request.getRequestURI());
        log.error("Unhandled exception on path {}", request.getRequestURI(), ex);
        return new ResponseEntity<>(body, status);
    }

    private ApiErrorResponse buildBody(HttpStatus status, ErrorCode errorCode, String message, String path) {
        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                errorCode.getCode(),
                message != null ? message : errorCode.getDefaultMessage(),
                path
        );
    }
}

