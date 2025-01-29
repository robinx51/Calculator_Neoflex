package ru.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.calculator.dto.ValidationError;
import ru.calculator.exception.BaseExceptionHandler;
import ru.deal.exception.FeignValidationException;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class StatementExceptionHandler extends BaseExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(StatementExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        return super.handleValidationErrors(ex, logger);
    }

    @ExceptionHandler(FeignValidationException.class)
    public ResponseEntity<List<ValidationError>> handleFeignValidationException(FeignValidationException ex) {
        logger.warn("Ошибка валидации при вызове микросервиса: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }
}