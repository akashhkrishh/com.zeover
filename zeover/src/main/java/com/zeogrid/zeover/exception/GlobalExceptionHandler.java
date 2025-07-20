package com.zeogrid.zeover.exception;

import com.zeogrid.zeover.payload.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<GlobalResponse<String>> handleInvalidArgumentException(InvalidArgumentException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GlobalResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<String>> handleException(Exception ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<GlobalResponse<String>> handleEmailNotFoundException(EmailNotFoundException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<GlobalResponse<String>> handleUserAlreadyRegisterException(UserAlreadyRegisterException ex) {
        return buildExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<GlobalResponse<String>> buildExceptionResponse(String errorMessage, HttpStatus status) {
        GlobalResponse<String> response = new GlobalResponse<>(null,null, errorMessage, false);
        return new ResponseEntity<>(response, status);
    }

}
