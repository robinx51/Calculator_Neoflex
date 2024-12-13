package Neoflex_bank.MS_calculator.annotations;

import Neoflex_bank.MS_calculator.dto.EmploymentDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmployerValidator implements ConstraintValidator<Employer, EmploymentDto.EmploymentStatus> {
    @Override
    public boolean isValid(EmploymentDto.EmploymentStatus status, ConstraintValidatorContext context) {
        return status != EmploymentDto.EmploymentStatus.UNEMPLOYED;
    }
}