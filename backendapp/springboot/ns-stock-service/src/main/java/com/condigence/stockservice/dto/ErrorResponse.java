package com.condigence.stockservice.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    // A short, machine-readable error code (e.g., STOCK_NOT_FOUND, TECHNICAL_ERROR)
    private String code;

    // Human-readable error message
    private String message;

    // HTTP status code value
    private int status;

    private LocalDateTime timestamp;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String code, String message, int status) {
        this();
        this.code = code;
        this.message = message;
        this.status = status;
    }

    // Convenience factory for business errors
    public static ErrorResponse business(String code, String message, int status) {
        return new ErrorResponse(code, message, status);
    }

    // Convenience factory for technical errors
    public static ErrorResponse technical(String message, int status) {
        return new ErrorResponse("TECHNICAL_ERROR", message, status);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
