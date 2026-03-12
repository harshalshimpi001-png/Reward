package com.transaction.reward.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException exception){
        Map<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("timestamp", LocalDateTime.now());
        errorMsg.put("status", HttpStatus.NOT_FOUND.value());
        errorMsg.put("error", exception.getMessage());
        return new ResponseEntity<>(errorMsg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDateRange(InvalidDateRangeException exception){
        Map<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("timestamp", LocalDateTime.now());
        errorMsg.put("status", HttpStatus.BAD_REQUEST.value());
        errorMsg.put("error", exception.getMessage());
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException exception){
        Map<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("timestamp", LocalDateTime.now());
        errorMsg.put("status", HttpStatus.BAD_REQUEST.value());
        errorMsg.put("error", exception.getMessage());
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception exception){
        Map<String, Object> errorMsg = new HashMap<>();
        errorMsg.put("timestamp", LocalDateTime.now());
        errorMsg.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorMsg.put("error", exception.getMessage());
        return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
