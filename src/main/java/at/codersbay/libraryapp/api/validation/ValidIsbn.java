package at.codersbay.libraryapp.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that a String is a valid ISBN-13.
 * An ISBN-13 must consist of exactly 13 digits (hyphens are allowed and ignored).
 * The check digit (last digit) is also validated against the ISBN-13 checksum algorithm.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsbnValidator.class)
public @interface ValidIsbn {

    String message() default "Invalid ISBN-13: must be exactly 13 digits with a valid check digit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
