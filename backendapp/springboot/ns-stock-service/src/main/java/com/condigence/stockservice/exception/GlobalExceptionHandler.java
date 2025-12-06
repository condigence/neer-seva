package com.condigence.stockservice.exception;

import com.condigence.stockservice.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---- Business exceptions ----

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStockNotFound(StockNotFoundException ex) {
        logger.warn("Stock not found: {}", ex.getMessage());
        ErrorResponse resp = ErrorResponse.business(
                "STOCK_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShopNotFound(ShopNotFoundException ex) {
        logger.warn("Shop not found: {}", ex.getMessage());
        ErrorResponse resp = ErrorResponse.business(
                "SHOP_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(ShopOperationException.class)
    public ResponseEntity<ErrorResponse> handleShopOperation(ShopOperationException ex) {
        logger.warn("Shop operation failed: {}", ex.getMessage());
        ErrorResponse resp = ErrorResponse.business(
                "SHOP_OPERATION_FAILED",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficient(InsufficientStockException ex) {
        logger.warn("Insufficient stock: {}", ex.getMessage());
        ErrorResponse resp = ErrorResponse.business(
                "INSUFFICIENT_STOCK",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrder(InvalidOrderException ex) {
        logger.warn("Invalid order: {}", ex.getMessage());
        ErrorResponse resp = ErrorResponse.business(
                "INVALID_ORDER",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation failed");
        logger.warn("Validation error: {}", message);
        ErrorResponse resp = ErrorResponse.business(
                "VALIDATION_ERROR",
                message,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    // ---- Technical / fallback exceptions ----

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        logger.error("Unhandled exception", ex);
        ErrorResponse resp = ErrorResponse.technical(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}
