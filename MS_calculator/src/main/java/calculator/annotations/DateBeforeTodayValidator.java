package calculator.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateBeforeTodayValidator implements ConstraintValidator<DateBeforeToday, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }
        return birthDate.isBefore(LocalDate.now());
    }
}
