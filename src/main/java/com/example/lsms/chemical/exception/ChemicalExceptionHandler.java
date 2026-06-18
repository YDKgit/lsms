package com.example.lsms.chemical.exception;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.example.lsms.chemical")
public class ChemicalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<String>> handleCustomException(CustomException e) {
        log.warn("Chemical CustomException: {}", e.getMessage());
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(CommonResponse.fail(code.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handleValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
        String message = fieldError == null
                ? ErrorCode.INVALID_INPUT.getMessage()
                : fieldError.getField() + ": " + fieldError.getDefaultMessage();
        log.warn("Chemical ValidationException: {}", message);
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .orElse(ErrorCode.INVALID_INPUT.getMessage());
        log.warn("Chemical ConstraintViolationException: {}", message);
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        log.warn("Chemical HttpMessageNotReadableException: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(ErrorCode.INVALID_INPUT.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<String>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e
    ) {
        log.warn("Chemical DataIntegrityViolationException: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(CommonResponse.fail(ErrorCode.INVALID_INPUT.getMessage()));
    }
}
