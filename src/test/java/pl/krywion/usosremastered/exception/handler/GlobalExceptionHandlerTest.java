package pl.krywion.usosremastered.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.exception.base.BaseException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBadCredentialsException() {
        // given
        BadCredentialsException exception = new BadCredentialsException("Invalid username or password");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleAuthenticationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid username or password");
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    void shouldHandleExpiredJwtException() {
        // given
        ExpiredJwtException exception = mock(ExpiredJwtException.class);

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleAuthenticationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Token has expired");
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    void shouldHandleSignatureException() {
        // given
        SignatureException exception = new SignatureException("test");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleAuthenticationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid token signature");
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    void shouldHandleAccessDeniedException() {
        // given
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleAccessDeniedException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied");
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    void shouldHandleValidationException() {
        // given
        ValidationException exception = new ValidationException("Validation failed");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleValidationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    void shouldHandleEntityValidationException() {
        // given
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        EntityValidationException exception = new EntityValidationException(Student.class, errors);

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleValidationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Validation failed for entity Student");
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getDetails())
                .containsEntry("entityType", "Student")
                .containsEntry("validationErrors", errors);
    }

    @Test
    void shouldHandleBaseException() {
        // given
        BaseException exception = new ResourceNotFoundException("Resource not found");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleBaseException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Resource not found");
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    void shouldHandleUnexpectedException() {
        // given
        RuntimeException exception = new RuntimeException("Unexpected error");

        // when
        ResponseEntity<ServiceResponse<?>> response = exceptionHandler.handleUnexpectedException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("An unexpected error occurred");
        assertThat(response.getBody().isSuccess()).isFalse();
    }
}