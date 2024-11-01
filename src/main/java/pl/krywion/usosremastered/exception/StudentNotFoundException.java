package pl.krywion.usosremastered.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String email) {
        super("Student with email: " + email + " not found");
    }

    public StudentNotFoundException(Long albumNumber) {
        super("Student with album number: " + albumNumber + " not found");
    }
}
