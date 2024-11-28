package MS_calculator.Annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateBeforeTodayValidator implements ConstraintValidator<DateBeforeToday, LocalDate> {
    @Override
    public boolean isValid(LocalDate issueDate, ConstraintValidatorContext context) {
        return issueDate.isBefore(LocalDate.now());
    }
}
