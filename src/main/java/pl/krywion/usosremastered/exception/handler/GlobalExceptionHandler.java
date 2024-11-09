package pl.krywion.usosremastered.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.krywion.usosremastered.dto.response.ApiResponse;
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
            AuthorizationDeniedException.class
    })
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(Exception ex) {
        String message;
        HttpStatus status;

        if (ex instanceof BadCredentialsException) {
            message = "Invalid username or password";
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof ExpiredJwtException) {
            message = "Token has expired";
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof SignatureException) {
            message = "Invalid token signature";
            status = HttpStatus.UNAUTHORIZED;
        } else {
            message = "Access denied";
            status = HttpStatus.FORBIDDEN;
        }

        return new ResponseEntity<>(ApiResponse.error(message, status), status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(ValidationException ex) {
        ApiResponse<?> response;

        if (ex instanceof EntityValidationException eve) {
            Map<String, Object> details = new HashMap<>();
            details.put("entityType", eve.getEntityClass().getSimpleName());
            details.put("validationErrors", eve.getValidationErrors());

            response = ApiResponse.error(
                    ex.getMessage(),
                    ex.getHttpStatus(),
                    details
            );
        } else {
            response = ApiResponse.error(
                    ex.getMessage(),
                    ex.getHttpStatus()
            );
        }

        return new ResponseEntity<>(response, ex.getHttpStatus());

    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ex.getMessage(),
                ex.getHttpStatus()
        );
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpectedException(Exception ex) {
        ApiResponse<?> response = ApiResponse.error(
            "An unexpected error occurred" + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
