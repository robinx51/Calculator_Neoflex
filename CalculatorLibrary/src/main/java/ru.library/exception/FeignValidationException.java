package ru.library.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.library.dto.ValidationError;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeignValidationException extends RuntimeException {
    private List<ValidationError> errors;
}