package at.codersbay.libraryapp.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates ISBN-13 format and checksum.
 * Strips hyphens, then checks:
 *   1. exactly 13 digits
 *   2. ISBN-13 checksum: sum of (digit * weight), where weight alternates 1 and 3, must be divisible by 10
 */
public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // @NotBlank handles the null/empty case
        }

        String digits = value.replace("-", "").replace(" ", "");

        if (!digits.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }
}
