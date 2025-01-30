package ru.library.annotations.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.library.annotations.validator.EmployerValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmployerValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Employer {
    String message() default "Рабочий статус не должен быть 'Безработный'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}