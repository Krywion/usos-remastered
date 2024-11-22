package pl.krywion.usosremastered.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Email Validator Tests")
class EmailValidatorTest {
    private final EmailValidator emailValidator = new EmailValidator();

    @Test
    @DisplayName("Should validate correct email")
    void shouldValidateCorrectEmail() {
        // given
        String correctEmail = "test@example.com";

        // when
        boolean isInvalid = emailValidator.isInvalid(correctEmail);

        // then
        assertFalse(isInvalid, "Email should be accepted: " + correctEmail);
    }

    @Test
    @DisplayName("Should validate correct email with subdomain")
    void shouldValidateCorrectEmailWithSubdomain() {
        // given
        String correctEmail = "test.email@subdomain.example.com";

        // when
        boolean isInvalid = emailValidator.isInvalid(correctEmail);

        // then
        assertFalse(isInvalid, "Email should be accepted: " + correctEmail);
    }

    @ParameterizedTest(name = "Should not validate email with incorrect format: {0}")
    @ValueSource(strings = {
            "test", // no domain
            "test@", // no domain
            "test@.", // no domain
            "test@com", // no domain
            "test@com.", // no domain
            "test@.com", // no domain
            "@example.com", // no local part
    })
    void shouldValidateIncorrectEmail(String incorrectEmail) {
        // when
        boolean isInvalid = emailValidator.isInvalid(incorrectEmail);

        // then
        assertTrue(isInvalid, "Email should be rejected: " + incorrectEmail);
    }

}
