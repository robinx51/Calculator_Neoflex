package ru.calculator.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class BaseExceptionHandler {

    protected ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex, Logger logger) {
        Map<String, String> errors = new HashMap<>();

        logger.warn("Обработка ошибки валидации: {}", ex.getMessage());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

            logger.info("Ошибка в поле '{}': {}", fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}