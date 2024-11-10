package pl.krywion.usosremastered.validation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;

@Component
@RequiredArgsConstructor
public class StudentDtoValidator extends AbstractDtoValidator<StudentDto> {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EmailValidator emailValidator;

    public void validate(StudentDto dto) {
        validateDto(dto, true);
    }

    public void validateForUpdate(StudentDto dto, Long albumNumber) {
        Student existingStudent = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        boolean isEmailChanged = !existingStudent.getEmail().equals(dto.getEmail());

        validateDto(dto, isEmailChanged);
    }

    @Override
    protected void validateDto(StudentDto dto) {
        validateDto(dto, true);
    }

    protected void validateDto(StudentDto dto, boolean checkEmailExists) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            addError("First name is required");
        }

        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            addError("Last name is required");
        }

        if (emailValidator.isInvalid(dto.getEmail())) {
            addError(emailValidator.getErrorMessage());
        }

        if (checkEmailExists && userRepository.existsByEmail(dto.getEmail())) {
            addError("User with this email already exists");
        }

        if (dto.getStudyPlanId() == null) {
            addError("Study plan ID is required");
        }
    }

    @Override
    protected Class<?> getEntityClass() {
        return StudentDto.class;
    }
}