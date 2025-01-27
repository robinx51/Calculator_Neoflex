package ru.calculator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.calculator.dto.ValidationError;
import ru.calculator.dto.ValidationErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CalculatorExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn(ex.getMessage());
        List<ValidationError> errors = ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> {
                    FieldError fieldError = (FieldError) error;
                    return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.toList());

        ValidationErrorResponse response = new ValidationErrorResponse(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}