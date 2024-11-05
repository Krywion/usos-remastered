package pl.krywion.usosremastered.validation.dto;

import jakarta.validation.ValidationException;
import pl.krywion.usosremastered.validation.core.Validator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDtoValidator<T> implements Validator<T> {
    protected final List<String> errors = new ArrayList<>();

    @Override
    public void validate(T dto) {
        errors.clear();
        validateDto(dto);
        if(!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    protected abstract void validateDto(T dto);

    protected void addError(String error) {
        errors.add(error);
    }
}
