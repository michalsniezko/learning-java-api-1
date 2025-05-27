package com.example.locations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCountryCode {
    String message() default "Invalid country code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
