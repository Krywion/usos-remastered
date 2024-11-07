package pl.krywion.usosremastered.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityValidationException extends ValidationException{
    private final Class<?> entityClass;
    private final List<String> validationErrors;

    public EntityValidationException(Class<?> entityClass, List<String> errors) {
        super(String.format("Validation failed for entity %s: %s",
                entityClass.getSimpleName()
                , String.join(", ", errors)));
        this.entityClass = entityClass;
        this.validationErrors = errors;
    }
}
