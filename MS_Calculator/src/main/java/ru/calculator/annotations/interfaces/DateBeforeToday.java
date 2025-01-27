package ru.calculator.annotations.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.calculator.annotations.validator.DateBeforeTodayValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateBeforeTodayValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateBeforeToday {
    String message() default "Дата должна быть до сегодняшнего дня";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
