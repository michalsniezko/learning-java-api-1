package com.example.locations.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;

public class CountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() != 2) {
            return false;
        }

        String upper = value.toUpperCase(Locale.ROOT);
        String[] isoCountries = Locale.getISOCountries();

        return Arrays.asList(isoCountries).contains(upper);
    }
}
