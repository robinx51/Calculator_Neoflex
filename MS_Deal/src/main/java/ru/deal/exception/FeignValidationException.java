package ru.deal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.calculator.dto.ValidationError;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeignValidationException extends RuntimeException {
    private List<ValidationError> errors;
}