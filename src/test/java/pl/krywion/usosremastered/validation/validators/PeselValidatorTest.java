package pl.krywion.usosremastered.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PESEL Validator Tests")
class PeselValidatorTest {
    private final PeselValidator peselValidator = new PeselValidator();

    @Test
    @DisplayName("Should validate correct PESEL")
    void shouldValidateIncorrectPesel() {
        // given
        String correctPesel = "83103175464";

        // when
        boolean isInvalid = peselValidator.isInvalid(correctPesel);

        // then
        assertFalse(isInvalid, "PESEL should be accepted: " + correctPesel);
    }

    @ParameterizedTest(name = "Should not validate PESEL with incorrect length: {0}")
    @ValueSource(strings = {
            "1234567890", // too short
            "123456789012345" // too long
    })
    void shouldNotValidatePeselWithIncorrectLength(String pesel) {
        // when
        boolean isInvalid = peselValidator.isInvalid(pesel);

        // then
        assertTrue(isInvalid, "PESEL with incorrect length should be rejected: " + pesel);
    }

    @ParameterizedTest(name = "Should not validate PESEL with incorrect checksum: {0}")
    @ValueSource(strings = {
            "83103175463", // incorrect checksum
            "11111111111" // incorrect checksum
    })
    void shouldNotValidatePeselWithIncorrectChecksum(String pesel) {
        // when
        boolean isInvalid = peselValidator.isInvalid(pesel);

        // then
        assertTrue(isInvalid, "PESEL with incorrect checksum should be rejected: " + pesel);
    }

    @ParameterizedTest(name = "Should not validate PESEL with non-numeric characters: {0}")
    @ValueSource(strings = {
            "8310317546a", // non-numeric character
            "8310317546!", // non-numeric character
            "8310317546 " // non-numeric character
    })
    void shouldNotValidatePeselWithNonNumericCharacters(String pesel) {
        // when
        boolean isInvalid = peselValidator.isInvalid(pesel);

        // then
        assertTrue(isInvalid, "PESEL with non-numeric characters should be rejected: " + pesel);
    }
}
