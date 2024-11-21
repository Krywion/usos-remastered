package pl.krywion.usosremastered.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ServiceResponse<?> serviceResponse = ServiceResponse.error(
                "Unauthorized - JWT token is missing or invalid",
                HttpStatus.UNAUTHORIZED
        );

        sendErrorResponse(response, serviceResponse, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        ServiceResponse<?> serviceResponse = ServiceResponse.error(
                "Access denied",
                HttpStatus.FORBIDDEN
        );

        sendErrorResponse(response, serviceResponse, HttpStatus.FORBIDDEN);
    }

    private void sendErrorResponse(HttpServletResponse response, ServiceResponse<?> serviceResponse,
                                   HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(serviceResponse));
    }

}