package pl.krywion.usosremastered.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class ApiResponse<T> {

    private T data;
    private String message;
    private boolean success;
    private HttpStatus httpStatus;
    private Map<String, Object> details;

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus httpStatus) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setMessage(message);
        response.setSuccess(true);
        response.setHttpStatus(httpStatus);
        return response;
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus httpStatus) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(null);
        response.setMessage(message);
        response.setSuccess(false);
        response.setHttpStatus(httpStatus);
        return response;
    }

    public static <T> ApiResponse<T> error(
            String message,
            HttpStatus httpStatus,
            Map<String, Object> details) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(null);
        response.setMessage(message);
        response.setSuccess(false);
        response.setHttpStatus(httpStatus);
        response.setDetails(details);
        return response;
    }


}
