package com.condigence.nsuserservice.exception;

import com.condigence.nsuserservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private HttpStatus mapBusinessStatus(ErrorCode code) {
        if (code == null) {
            return HttpStatus.BAD_REQUEST;
        }
        switch (code) {
            case USER_ALREADY_REGISTERED:
                return HttpStatus.CONFLICT;          // 409
            case USER_NOT_FOUND:
            case ADDRESS_NOT_FOUND:
                return HttpStatus.NOT_FOUND;         // 404
            case CONTACT_MISSING:
            case INVALID_OTP:
                return HttpStatus.BAD_REQUEST;       // 400
            default:
                return HttpStatus.BAD_REQUEST;
        }
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleBusinessException(BusinessException ex) {
        logger.warn("Business exception: {} - {}", ex.getErrorCode().getCode(), ex.getMessage());
        CustomErrorType error = new CustomErrorType(ex.getMessage());
        error.setErrorType(ex.getErrorCode().getCode());
        HttpStatus status = mapBusinessStatus(ex.getErrorCode());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(TechnicalException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleTechnicalException(TechnicalException ex) {
        logger.error("Technical exception: {} - {}", ex.getErrorCode().getCode(), ex.getMessage(), ex);
        CustomErrorType error = new CustomErrorType(ex.getMessage());
        error.setErrorType(ex.getErrorCode().getCode());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .orElse("Validation failed");
        CustomErrorType error = new CustomErrorType(message);
        error.setErrorType(ErrorCode.CONTACT_MISSING.getCode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleGenericException(Exception ex) {
        logger.error("Unhandled exception", ex);
        CustomErrorType error = new CustomErrorType("An unexpected error occurred");
        error.setErrorType(ErrorCode.INTERNAL_ERROR.getCode());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
