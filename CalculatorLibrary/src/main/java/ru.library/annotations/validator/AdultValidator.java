package ru.library.annotations.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.library.annotations.interfaces.Adult;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    private int over;
    private int under;

    @Override
    public void initialize(Adult constraintAnnotation) {
        over = constraintAnnotation.over();
        under = constraintAnnotation.under();
    }
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return false;
        }

        int age = Period.between(birthdate, LocalDate.now()).getYears();

        return age >= over && age <= under;
    }
}
