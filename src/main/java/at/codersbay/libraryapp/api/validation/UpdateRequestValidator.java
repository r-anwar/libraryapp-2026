package at.codersbay.libraryapp.api.validation;

import at.codersbay.libraryapp.api.book.UpdateBookRequestDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateRequestValidator implements ConstraintValidator<ValidUpdateRequest, UpdateBookRequestDTO> {

    @Override
    public boolean isValid(UpdateBookRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        boolean hasId = dto.getId() != null;
        boolean hasIsbn = dto.getIsbn() != null && !dto.getIsbn().isBlank();

        return hasId || hasIsbn;
    }
}
