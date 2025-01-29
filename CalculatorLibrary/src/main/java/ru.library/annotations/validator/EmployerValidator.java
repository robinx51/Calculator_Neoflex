package ru.library.annotations.validator;

import ru.library.annotations.interfaces.Employer;
import ru.library.dto.EmploymentDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmployerValidator implements ConstraintValidator<Employer, EmploymentDto.EmploymentStatus> {
    @Override
    public boolean isValid(EmploymentDto.EmploymentStatus status, ConstraintValidatorContext context) {
        return status != EmploymentDto.EmploymentStatus.UNEMPLOYED;
    }
}