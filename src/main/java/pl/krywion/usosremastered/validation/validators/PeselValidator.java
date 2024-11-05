package pl.krywion.usosremastered.validation.validators;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.validation.core.FieldValidator;

@Component
public class PeselValidator implements FieldValidator<String> {
    @Override
    public boolean isInvalid(String pesel) {
        if (pesel == null || pesel.length() != 11) {
            return true;
        }

        try {
            int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
            int sum = 0;

            for (int i = 0; i < weights.length; i++) {
                sum += Integer.parseInt(pesel.substring(i, i + 1)) * weights[i];
            }

            int checksum = 10 - (sum % 10);
            if (checksum == 10) checksum = 0;

            return Character.getNumericValue(pesel.charAt(10)) != checksum;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Invalid PESEL number";
    }
}
