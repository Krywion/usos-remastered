package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
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
import pl.krywion.usosremastered.service.EmployeeService;
import pl.krywion.usosremastered.service.UserService;
import pl.krywion.usosremastered.validation.dto.EmployeeDtoValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoValidator employeeValidator;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    public EmployeeServiceImpl(
                               EmployeeRepository employeeRepository,
                               EmployeeDtoValidator employeeValidator,
                               DepartmentRepository departmentRepository, CourseRepository courseRepository,
                               EmployeeMapper employeeMapper,
                               UserService userService

    ) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.employeeMapper = employeeMapper;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        employeeValidator.validate(employeeDto);

        Department department = null;
        if(employeeDto.getDepartmentId() != null) {
            department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Department with id %d not found", employeeDto.getDepartmentId())
                    ));
        }



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
            Employee employee = employeeMapper.toEntity(employeeDto);
            employee.setHireDate(LocalDate.now());
            employee.setDepartment(department);
            employee.setCourses(courses);

            User createdUser = userService.createUser(employeeDto.getEmail(), Role.EMPLOYEE);
            employee.setUser(createdUser);

            Employee savedEmployee = employeeRepository.save(employee);
            EmployeeDto createdEmployeeDto = employeeMapper.toDto(savedEmployee);
            log.info("Employee created successfully: {}", savedEmployee);

            return ServiceResponse.success(
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
    @Transactional(readOnly = true)
    public ServiceResponse<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<EmployeeDto> updateEmployee(String pesel, EmployeeDto employeeDto) {
        try {
            employeeValidator.validateForUpdate(employeeDto, pesel);

            Employee employee = employeeRepository.findByPesel(pesel)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Employee with PESEL %s not found", pesel)
                    ));

            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());

            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Department with id %d not found", employeeDto.getDepartmentId())
                    ));

            employee.setDepartment(department);

            if(employeeDto.getCourseIds() != null) {
                List<Course> courses = employeeDto.getCourseIds().stream()
                        .map(courseId -> courseRepository.findById(courseId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        String.format("Course with id %d not found", courseId)
                                )))
                        .toList();
                employee.setCourses(courses);
            }

            if(!employee.getUser().getEmail().equals(employeeDto.getEmail())) {
                User user = userService.updateUser(employee.getUser().getEmail(), Role.EMPLOYEE);
                employee.setUser(user);
            }

            return  ServiceResponse.success(
                    employeeMapper.toDto(employee),
                    "Employee updated successfully",
                    HttpStatus.OK
            );
        } catch (
                Exception e) {
            if(e instanceof BaseException) {
                throw e;
            }

            log.error("Failed to update employee: ", e);
            throw new ValidationException("Failed to update employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<EmployeeDto> deleteEmployee(String pesel) {
        Employee employee = employeeRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Employee with PESEL %s not found", pesel)
                ));

        if(employee.getUser() != null) {
            userService.deleteUser(employee.getUser().getEmail());
        }

        employeeRepository.delete(employee);
        log.info("Employee deleted successfully: {}", employee);

        return ServiceResponse.success(
                employeeMapper.toDto(employee),
                "Employee deleted successfully",
                HttpStatus.OK
        );
    }


    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<EmployeeDto> getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Employee with email %s not found", email)
                ));
        return ServiceResponse.success(
                employeeMapper.toDto(employee),
                "Employee found successfully",
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName) {
        List<Employee> employees = employeeRepository.findByLastNameContainingIgnoreCase(lastName);
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<EmployeeDto> getEmployeeByPesel(String pesel) {
        Employee employee = employeeRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Employee with PESEL %s not found", pesel)
                ));
        return ServiceResponse.success(
                employeeMapper.toDto(employee),
                "Employee found successfully",
                HttpStatus.OK);

    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse<List<EmployeeDto>> getEmployeeByFirstName(String firstName) {
        List<Employee> employees = employeeRepository.findByFirstNameContainingIgnoreCase(firstName);
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }

}
