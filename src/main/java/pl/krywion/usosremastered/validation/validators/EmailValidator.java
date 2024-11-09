package pl.krywion.usosremastered.validation.validators;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.validation.core.FieldValidator;

import java.util.regex.Pattern;

@Component
public class EmailValidator implements FieldValidator<String> {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean isInvalid(String email) {
        return email == null || !EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public String getErrorMessage() {
        return "Invalid email format";
    }
}
