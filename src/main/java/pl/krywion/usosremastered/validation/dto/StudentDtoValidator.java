package pl.krywion.usosremastered.validation.dto;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;

@Component
public class StudentDtoValidator extends AbstractDtoValidator<StudentDto> {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;

    public StudentDtoValidator(UserRepository userRepository, EmailValidator emailValidator) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
    }

    @Override
    protected void validateDto(StudentDto dto) {

        if (emailValidator.isInvalid(dto.getEmail())) {
            addError(emailValidator.getErrorMessage());
        }

        if (dto.getStudyPlanId() == null) {
            addError("Study plan ID is required");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            addError("User with this email already exists");
        }

    }
}
