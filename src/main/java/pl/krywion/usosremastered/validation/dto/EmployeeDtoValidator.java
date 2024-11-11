package pl.krywion.usosremastered.validation.dto;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.CourseRepository;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public EmployeeDtoValidator(EmailValidator emailValidator,
                                PeselValidator peselValidator,
                                UserRepository userRepository,
                                DepartmentRepository departmentRepository,
                                CourseRepository courseRepository,
                                EmployeeRepository employeeRepository) {
        this.emailValidator = emailValidator;
        this.peselValidator = peselValidator;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void validateDto(EmployeeDto dto) {
        validateBasicInformation(dto, true);
        validateOrganizationalUnits(dto);
        validateCourses(dto);
    }

    @Override
    protected Class<?> getEntityClass() {
        return EmployeeDto.class;
    }

    private void validateBasicInformation(EmployeeDto dto, boolean checkEmailExists) {
        if(dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            addError("First name is required");
        }

        if(dto.getLastName() == null || dto.getLastName().isEmpty()) {
            addError("Last name is required");
        }

        if(emailValidator.isInvalid(dto.getEmail())) {
            addError(emailValidator.getErrorMessage());
        }

        if(checkEmailExists && userRepository.existsByEmail(dto.getEmail())) {
            addError("User with this email already exists");
        }

        if(peselValidator.isInvalid(dto.getPesel())) {
            addError(peselValidator.getErrorMessage());
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

    public void validateForUpdate(EmployeeDto dto, String pesel) {
        Employee existingEmployee = employeeRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        boolean isEmailChanged = !existingEmployee.getEmail().equals(dto.getEmail());

        if (!existingEmployee.getPesel().equals(dto.getPesel())) {
            addError("PESEL cannot be modified");
        }

        validateBasicInformation(dto, isEmailChanged);
        validateOrganizationalUnits(dto);
        validateCourses(dto);
    }
}
