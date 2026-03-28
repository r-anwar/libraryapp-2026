package at.codersbay.libraryapp.validation;

import at.codersbay.libraryapp.api.validation.IsbnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsbnValidatorTest {

    private IsbnValidator validator;

    @BeforeEach
    void setUp() {
        validator = new IsbnValidator();
    }

    @Test
    void isValid_validIsbn13_returnsTrue() {
        assertThat(validator.isValid("9780132350884", null)).isTrue();
    }

    @Test
    void isValid_validIsbn13WithHyphens_returnsTrue() {
        assertThat(validator.isValid("978-0-13-235088-4", null)).isTrue();
    }

    @Test
    void isValid_invalidCheckDigit_returnsFalse() {
        assertThat(validator.isValid("9780132350885", null)).isFalse();
    }

    @Test
    void isValid_tooShort_returnsFalse() {
        assertThat(validator.isValid("978013235088", null)).isFalse();
    }

    @Test
    void isValid_containsLetters_returnsFalse() {
        assertThat(validator.isValid("978013235088X", null)).isFalse();
    }

    @Test
    void isValid_nullValue_returnsTrue() {
        // null is handled by @NotBlank — validator must return true
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void isValid_blankValue_returnsTrue() {
        // blank is handled by @NotBlank — validator must return true
        assertThat(validator.isValid("   ", null)).isTrue();
    }
}
