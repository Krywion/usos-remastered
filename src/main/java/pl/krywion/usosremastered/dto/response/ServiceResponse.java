package pl.krywion.usosremastered.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@Schema(description = "Generic API Response wrapper")
public class ServiceResponse<T> {

    @Schema(description = "Response payload data")
    private T data;

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Operation success status", example = "true")
    private boolean success;

    @Schema(description = "HTTP status code", example = "200")
    private HttpStatus httpStatus;

    @Schema(description = "Additional response details")
    private Map<String, Object> details;

    public static <T> ServiceResponse<T> success(T data, String message, HttpStatus httpStatus) {
        ServiceResponse<T> response = new ServiceResponse<>();
        response.setData(data);
        response.setMessage(message);
        response.setSuccess(true);
        response.setHttpStatus(httpStatus);
        return response;
    }

    public static <T> ServiceResponse<T> error(String message, HttpStatus httpStatus) {
        ServiceResponse<T> response = new ServiceResponse<>();
        response.setData(null);
        response.setMessage(message);
        response.setSuccess(false);
        response.setHttpStatus(httpStatus);
        return response;
    }

    public static <T> ServiceResponse<T> error(
            String message,
            HttpStatus httpStatus,
            Map<String, Object> details) {
        ServiceResponse<T> response = new ServiceResponse<>();
        response.setData(null);
        response.setMessage(message);
        response.setSuccess(false);
        response.setHttpStatus(httpStatus);
        response.setDetails(details);
        return response;
    }


}
