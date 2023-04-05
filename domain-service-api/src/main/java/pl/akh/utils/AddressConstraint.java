package pl.akh.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = {AddressValidator.class})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface AddressConstraint {
    String message() default "Not valid address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}