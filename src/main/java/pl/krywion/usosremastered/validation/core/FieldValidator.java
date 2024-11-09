package pl.krywion.usosremastered.validation.core;

public interface FieldValidator<T> {
    boolean isInvalid(T t);
    String getErrorMessage();
}
