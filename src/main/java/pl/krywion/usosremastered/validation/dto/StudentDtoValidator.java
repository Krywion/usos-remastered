package pl.krywion.usosremastered.validation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;

@Component
@RequiredArgsConstructor
public class StudentDtoValidator extends AbstractDtoValidator<StudentDto> {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EmailValidator emailValidator;
    private final StudyPlanRepository studyPlanRepository;

    @Override
    protected void validateDto(StudentDto dto) {
        validateDto(dto, true);
    }

    @Override
    protected void validateForUpdateDto(StudentDto dto, Object albumNumber) {
        Student existingStudent = studentRepository.findById((Long) albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        boolean isEmailChanged = !existingStudent.getEmail().equals(dto.getEmail());

        validateDto(dto, isEmailChanged);
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
            addError("Email already exists");

        }

        if (studyPlanRepository.findById(dto.getStudyPlanId()).isEmpty()) {
            addError(String.format("Study plan with id %d not found", dto.getStudyPlanId()));
        }
    }

    @Override
    protected Class<?> getEntityClass() {
        return StudentDto.class;
    }
}