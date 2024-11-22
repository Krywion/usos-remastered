package pl.krywion.usosremastered.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.exception.base.BaseException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BadCredentialsException.class,
            ExpiredJwtException.class,
            SignatureException.class,
            MalformedJwtException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ServiceResponse<Void>> handleAuthenticationException(Exception ex) {
        String message;
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if (ex instanceof BadCredentialsException) {
            message = "Invalid username or password";
        } else if (ex instanceof ExpiredJwtException) {
            message = "Token has expired";
        } else if (ex instanceof SignatureException) {
            message = "Invalid token signature";
        } else if (ex instanceof MalformedJwtException) {
            message = "Malformed JWT token";
        } else {
            message = "Authentication failed";
        }

        return ResponseEntity.status(status)
                .body(ServiceResponse.error(message, status));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServiceResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ServiceResponse.error("Access denied", HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ServiceResponse<Void>> handleValidationException(ValidationException ex) {
        ServiceResponse<Void> response;

        if (ex instanceof EntityValidationException eve) {
            Map<String, Object> details = new HashMap<>();
            details.put("entityType", eve.getEntityClass().getSimpleName());
            details.put("validationErrors", eve.getValidationErrors());

            response = ServiceResponse.error(
                    ex.getMessage(),
                    ex.getHttpStatus(),
                    details
            );
        } else {
            response = ServiceResponse.error(
                    ex.getMessage(),
                    ex.getHttpStatus()
            );
        }

        return new ResponseEntity<>(response, ex.getHttpStatus());

    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ServiceResponse<Void>> handleBaseException(BaseException ex) {
        ServiceResponse<Void> response = ServiceResponse.error(
                ex.getMessage(),
                ex.getHttpStatus()
        );
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse<Void>> handleUnexpectedException(Exception ex) {
        ServiceResponse<Void> response = ServiceResponse.error(
            "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
