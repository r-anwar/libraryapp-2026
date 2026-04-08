package at.codersbay.libraryapp.validation;

import at.codersbay.libraryapp.api.book.UpdateBookRequestDTO;
import at.codersbay.libraryapp.api.validation.UpdateRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestValidatorTest {

    private UpdateRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UpdateRequestValidator();
    }

    @Test
    void isValid_withId_returnsTrue() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO();
        dto.setId(1L);

        assertThat(validator.isValid(dto, null)).isTrue();
    }

    @Test
    void isValid_withIsbn_returnsTrue() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO();
        dto.setIsbn("9780132350884");

        assertThat(validator.isValid(dto, null)).isTrue();
    }

    @Test
    void isValid_withBothIdAndIsbn_returnsTrue() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO();
        dto.setId(1L);
        dto.setIsbn("9780132350884");

        assertThat(validator.isValid(dto, null)).isTrue();
    }

    @Test
    void isValid_noIdAndNoIsbn_returnsFalse() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO();
        dto.setTitle("Some Title");

        assertThat(validator.isValid(dto, null)).isFalse();
    }

    @Test
    void isValid_blankIsbnAndNoId_returnsFalse() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO();
        dto.setIsbn("   ");

        assertThat(validator.isValid(dto, null)).isFalse();
    }

    @Test
    void isValid_nullDto_returnsTrue() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}
