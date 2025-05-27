package com.example.locations.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CountryCodeValidatorTest {
    private CountryCodeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CountryCodeValidator();
    }

    @Test
    void validCountryCodesShouldPass() {
        assertTrue(validator.isValid("PL", mock(ConstraintValidatorContext.class)));
        assertTrue(validator.isValid("us", mock(ConstraintValidatorContext.class)));
    }

    @Test
    void invalidCountryCodesShouldFail() {
        assertFalse(validator.isValid("ZZ", mock(ConstraintValidatorContext.class)));
        assertFalse(validator.isValid("abc", mock(ConstraintValidatorContext.class)));
    }

    @Test
    void nullValueIsNotValid() {
        assertFalse(validator.isValid(null, mock(ConstraintValidatorContext.class)));
    }
}