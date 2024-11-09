package pl.krywion.usosremastered.validation.dto;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.repository.CourseRepository;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.validation.validators.EmailValidator;
import pl.krywion.usosremastered.validation.validators.PeselValidator;

@Component
public class EmployeeDtoValidator extends AbstractDtoValidator<EmployeeDto> {

    private final EmailValidator emailValidator;
    private final PeselValidator peselValidator;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public EmployeeDtoValidator(EmailValidator emailValidator,
                                PeselValidator peselValidator,
                                UserRepository userRepository,
                                DepartmentRepository departmentRepository,
                                CourseRepository courseRepository) {
        this.emailValidator = emailValidator;
        this.peselValidator = peselValidator;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    protected void validateDto(EmployeeDto dto) {
        validateBasicInformation(dto);
        validateOrganizationalUnits(dto);
        validateCourses(dto);
    }

    @Override
    protected Class<?> getEntityClass() {
        return EmployeeDto.class;
    }

    private void validateBasicInformation(EmployeeDto dto) {
        if(dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            addError("First name is required");
        }

        if(dto.getLastName() == null || dto.getLastName().isEmpty()) {
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

    private void validateOrganizationalUnits(EmployeeDto dto) {
        if(dto.getDepartmentId() != null) {
            if(departmentRepository.findById(dto.getDepartmentId()).isEmpty()) {
                addError("Department not found with id: " + dto.getDepartmentId());
            }
        }
    }

    private void validateCourses(EmployeeDto dto) {
        if(dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            dto.getCourseIds().forEach(courseId -> {
                if(courseRepository.findById(courseId).isEmpty()) {
                    addError("Course not found with id: " + courseId);
                }
            });
        }
    }
}
