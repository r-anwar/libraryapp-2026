package at.codersbay.libraryapp.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Class-level constraint that validates an update request DTO
 * has at least one identifier (id or isbn) set, so the entity
 * to update can be located.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UpdateRequestValidator.class)
public @interface ValidUpdateRequest {

    String message() default "Either 'id' or 'isbn' must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
