package ru.calculator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AdultValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    int over() default 18;
    int under() default 65;

    String message() default "Возраст должен быть более {over} и менее {under} лет";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
