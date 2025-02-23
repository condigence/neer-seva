package com.condigence.bookingservice.controller;


import com.condigence.bookingservice.util.CustomErrorType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// Safety Net for errors
@ControllerAdvice
@AllArgsConstructor
public class ErrorController {

    public static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorType> handleError(final Exception ex) {
       return new ResponseEntity<>(new CustomErrorType("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /// Handle other exceptions
}
