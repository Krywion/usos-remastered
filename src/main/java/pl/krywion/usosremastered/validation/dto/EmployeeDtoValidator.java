package pl.krywion.usosremastered.validation.dto;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;
import pl.krywion.usosremastered.validation.validators.PeselValidator;

@Component
public class EmployeeDtoValidator extends AbstractDtoValidator<EmployeeDto> {

    private final EmailValidator emailValidator;
    private final PeselValidator peselValidator;
    private final UserRepository userRepository;

    public EmployeeDtoValidator(EmailValidator emailValidator, PeselValidator peselValidator, UserRepository userRepository) {
        this.emailValidator = emailValidator;
        this.peselValidator = peselValidator;
        this.userRepository = userRepository;
    }

    @Override
    protected void validateDto(EmployeeDto dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            addError("First name is required");
        }

        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            addError("Last name is required");
        }

        if(emailValidator.isInvalid(dto.getEmail())) {
            addError(emailValidator.getErrorMessage());
        }

        if(peselValidator.isInvalid(dto.getPesel())) {
            addError(peselValidator.getErrorMessage());
        }

        if(userRepository.existsByEmail(dto.getEmail())) {
            addError("User with this email already exists");
        }
    }
}
