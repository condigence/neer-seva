package com.condigence.nsproductservice.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Standard API error response returned by the global exception handler.
 */
@Schema(name = "ApiErrorResponse", description = "Standard error payload returned when an API call fails.",
        example = "{\n  \"timestamp\": \"2025-12-06T10:15:30.123Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"code\": \"RESOURCE_NOT_FOUND\",\n  \"message\": \"Requested resource was not found\",\n  \"path\": \"/neerseva/api/v1/products/brands/999\"\n}")
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(Instant timestamp, int status, String error, String code, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
