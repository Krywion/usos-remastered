package pl.krywion.usosremastered.exception;

import org.springframework.http.HttpStatus;
import pl.krywion.usosremastered.exception.base.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
