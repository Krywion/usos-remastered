package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;
import pl.krywion.usosremastered.entity.Course;
import pl.krywion.usosremastered.entity.Department;
import pl.krywion.usosremastered.entity.Employee;

import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.exception.base.BaseException;
import pl.krywion.usosremastered.repository.CourseRepository;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.EmployeeService;
import pl.krywion.usosremastered.validation.dto.EmployeeDtoValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoValidator employeeValidator;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public EmployeeServiceImpl(AuthenticationService authenticationService,
                               ModelMapper modelMapper,
                               EmployeeRepository employeeRepository,
                               EmployeeDtoValidator employeeValidator,
                               DepartmentRepository departmentRepository, CourseRepository courseRepository) {
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        employeeValidator.validate(employeeDto);

        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Department with id %d not found", employeeDto.getDepartmentId())
                ));

        List<Course> courses = new ArrayList<>();
        if (employeeDto.getCourseIds() != null && !employeeDto.getCourseIds().isEmpty()) {
            courses = employeeDto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    String.format("Course with id %d not found", courseId)
                            )))
                    .collect(Collectors.toList());
        }

        try {
            Employee employee = modelMapper.map(employeeDto, Employee.class);
            employee.setHireDate(LocalDate.now());
            employee.setDepartment(department);
            employee.setCourses(courses);

            RegisterUserDto registerUserDto = new RegisterUserDto();
            registerUserDto.setEmail(employeeDto.getEmail());
            registerUserDto.setRole(Role.EMPLOYEE);

            User createdUser = authenticationService.signUp(registerUserDto);
            employee.setUser(createdUser);

            Employee savedEmployee = employeeRepository.save(employee);
            EmployeeDto createdEmployeeDto = modelMapper.map(savedEmployee, EmployeeDto.class);

            log.info("Employee created successfully: {}", savedEmployee);

            return ApiResponse.success(
                    createdEmployeeDto,
                    "Employee created successfully",
                    HttpStatus.CREATED
            );
    } catch (Exception e) {
            if(e instanceof BaseException) {
                throw e;
            }

            log.error("Failed to create student: ", e);
            throw new ValidationException("Failed to create student: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<EmployeeDto> getEmployeeByEmail(String email) {
        return null;
    }

    @Override
    public ApiResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName) {
        return null;
    }

    @Override
    public ApiResponse<List<EmployeeDto>> getAllEmployees() {
        return null;
    }

    @Override
    public ApiResponse<EmployeeDto> deleteEmployee(Long employeeId) {
        return null;
    }


}
