package pl.krywion.usosremastered.exception;

import org.springframework.http.HttpStatus;
import pl.krywion.usosremastered.exception.base.BaseException;

public class ValidationException extends BaseException {
  public ValidationException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
