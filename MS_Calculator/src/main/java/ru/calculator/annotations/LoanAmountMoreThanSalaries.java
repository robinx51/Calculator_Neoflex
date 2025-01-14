package ru.calculator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoanAmountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoanAmountMoreThanSalaries {
    String message() default "Сумма займа должна быть больше, чем 24 зарплат";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
