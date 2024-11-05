package pl.krywion.usosremastered.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudyPlanNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudyPlanNotFoundException(StudyPlanNotFoundException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(StudentCreationException.class)
    public ResponseEntity<Map<String, String>> handleStudentCreationException(StudentCreationException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFoundException(StudentNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmployeeCreationException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeCreationException(EmployeeCreationException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(Exception ex) {
        if (ex instanceof BadCredentialsException) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        if (ex instanceof AuthorizationDeniedException) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }
        if (ex instanceof ExpiredJwtException) {
            return ResponseEntity.status(401).body(Map.of("error", "Token expired"));
        }
        if (ex instanceof SignatureException) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid token"));
        }
        return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
}
