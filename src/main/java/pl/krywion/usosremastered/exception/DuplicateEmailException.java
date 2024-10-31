package pl.krywion.usosremastered.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email: " + email + " already exists");
    }
}
