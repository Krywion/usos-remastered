package pl.krywion.usosremastered.service.employee.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.entity.*;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.CourseRepository;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.service.UserService;
import pl.krywion.usosremastered.validation.dto.EmployeeDtoValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeCommandHandler {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final EmployeeMapper mapper;
    private final EmployeeDtoValidator validator;
    private final UserService userService;

    public Employee handle(CreateEmployeeCommand command) {
        try {
            validator.validate(command.employeeDto());

            Employee employee = mapper.toEntity(command.employeeDto());
            setupEmployeeRelation(employee, command.employeeDto());

            return employeeRepository.save(employee);
        } catch (EntityValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating employee", e);
            throw e;
        }
    }

    public Employee handle(UpdateEmployeeCommand command) {
        try {
            validator.validateForUpdate(command.employeeDto(), command.pesel());

            Employee employee = employeeRepository.findByPesel(command.pesel())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Employee with PESEL %s not found", command.pesel())
                    ));

            updateEmployeeData(employee, command.employeeDto());

            return employeeRepository.save(employee);
        } catch (EntityValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating employee", e);
            throw e;
        }
    }

    public Employee handle(DeleteEmployeeCommand command) {
        Employee employee = employeeRepository.findByPesel(command.pesel())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Employee with PESEL %s not found", command.pesel())
                ));

        if(employee.getUser() != null) {
            userService.deleteUser(employee.getUser().getEmail());
        }

        employeeRepository.delete(employee);
        return employee;
    }

    private void setupEmployeeRelation(Employee employee, EmployeeDto dto) {
        if(dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Department with id %d not found", dto.getDepartmentId())));
            employee.setDepartment(department);
        }

        if(dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = dto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new ResourceNotFoundException(String.format("Course with id %d not found", courseId)))
                    ).toList();
            employee.setCourses(new ArrayList<>(courses));
        } else {
            employee.setCourses(new ArrayList<>());
        }

        User user = userService.createUserForEmployee(dto.getEmail());
        employee.setUser(user);
        employee.setHireDate(LocalDate.now());
    }

    private void updateEmployeeData(Employee employee, EmployeeDto dto) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());

        if (dto.getDepartmentId() != null &&
                (employee.getDepartment() == null || !employee.getDepartment().getId().equals(dto.getDepartmentId()))) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Department with id %d not found", dto.getDepartmentId())));
            employee.setDepartment(department);
        }

        updateCourses(employee, dto.getCourseIds());

        if (!employee.getUser().getEmail().equals(dto.getEmail())) {
            userService.updateUserEmail(employee.getUser().getEmail(), dto.getEmail());
        }
    }

    private void updateCourses(Employee employee, List<Long> newCourseIds) {
        if (newCourseIds == null) {
            return;
        }

        Set<Long> currentCourseIds = employee.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        Set<Long> newCourseIdsSet = new HashSet<>(newCourseIds);

        if (currentCourseIds.equals(newCourseIdsSet)) {
            return;
        }

        List<Course> updatedCourses = newCourseIds.stream()
                .map(courseId -> courseRepository.findById(courseId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("Course with id %d not found", courseId)
                        )))
                .toList();
        employee.setCourses(new ArrayList<>(updatedCourses));
    }
}

