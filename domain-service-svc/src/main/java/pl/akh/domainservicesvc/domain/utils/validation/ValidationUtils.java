package pl.akh.domainservicesvc.domain.utils.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;

import java.util.Objects;
import java.util.Set;

public class ValidationUtils {

    public static <T> void validateEntity(T entity, Validator validator) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> String convertToMessage(Set<ConstraintViolation<T>> violations) {
        StringBuilder stringBuilder = new StringBuilder();
        violations.stream()
                .filter(Objects::nonNull)
                .forEach(violation -> {
                    stringBuilder.append(violation.getMessage());
                    stringBuilder.append("\n");
                });
        return stringBuilder.toString();
    }
}
