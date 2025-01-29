package ru.deal.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import jakarta.validation.ValidationException;
import ru.library.dto.ValidationError;
import ru.library.exception.BaseExceptionHandler;
import ru.library.exception.FeignValidationException;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class DealExceptionHandler extends BaseExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex, WebRequest request) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

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