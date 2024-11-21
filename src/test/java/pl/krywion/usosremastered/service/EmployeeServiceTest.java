package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.entity.*;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.CourseRepository;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.service.employee.EmployeeServiceImpl;
import pl.krywion.usosremastered.service.employee.command.EmployeeCommandHandler;
import pl.krywion.usosremastered.service.employee.query.EmployeeQueryServiceImpl;
import pl.krywion.usosremastered.validation.dto.EmployeeDtoValidator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Employee Service Tests")
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeDtoValidator validator;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(
                new EmployeeCommandHandler(employeeRepository, departmentRepository, courseRepository, employeeMapper, validator, userService),
                new EmployeeQueryServiceImpl(employeeRepository)
        );
    }

    @Test
    @DisplayName("Should create employee")
    void shouldCreateEmployee() {
        // given
        EmployeeDto dto = createEmployeeDto();
        Employee employee = createEmployee();
        User user = new User();
        user.setEmail("test@example.com");
        Department department = new Department();
        department.setId(1L);

        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(userService.createUserForEmployee(dto.getEmail())).thenReturn(user);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // when
        Employee result = employeeService.createEmployee(dto);

        // then
        assertThat(result).isNotNull();
        verify(validator).validate(dto);
        verify(employeeRepository).save(any(Employee.class));
        verify(userService).createUserForEmployee(dto.getEmail());
    }

    @Test
    @DisplayName("Should get all employees")
    void shouldGetAllEmployees() {
        // given
        List<Employee> employees = Arrays.asList(createEmployee(), createEmployee());
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<Employee> result = employeeService.getAllEmployees();

        // then
        assertThat(result).hasSize(2);
        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("Should update employee by PESEL")
    void shouldUpdateEmployee() {
        // given
        String pesel = "12345678901";
        EmployeeDto dto = createEmployeeDto();
        Employee existingEmployee = createEmployee();
        Department department = new Department();
        department.setId(1L);

        when(employeeRepository.findByPesel(pesel)).thenReturn(Optional.of(existingEmployee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        // when
        Employee result = employeeService.updateEmployee(pesel, dto);

        // then
        assertThat(result).isNotNull();
        verify(validator).validateForUpdate(dto, pesel);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should delete employee by PESEL")
    void shouldDeleteEmployee() {
        // given
        String pesel = "12345678901";
        Employee employee = createEmployee();
        User user = new User();
        user.setEmail("test@example.com");
        employee.setUser(user);

        when(employeeRepository.findByPesel(pesel)).thenReturn(Optional.of(employee));

        // when
        employeeService.deleteEmployee(pesel);

        // then
        verify(employeeRepository).delete(employee);
        verify(userService).deleteUser(user.getEmail());
    }

    @Test
    @DisplayName("Should delete employee by PESEL")
    void shouldThrowExceptionWhenEmployeeNotFound() {
        // given
        String pesel = "12345678901";
        when(employeeRepository.findByPesel(pesel)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> employeeService.getEmployeeByPesel(pesel))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should get employee by LastName")
    void shouldGetEmployeesByLastName() {
        // given
        List<Employee> employees = Collections.singletonList(createEmployee());
        when(employeeRepository.findByLastNameContainingIgnoreCase("Test")).thenReturn(employees);

        // when
        List<Employee> result = employeeService.getEmployeesByLastName("Test");

        // then
        assertThat(result).hasSize(1);
        verify(employeeRepository).findByLastNameContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("Should get employee by FirstName")
    void shouldGetEmployeeByFirstName() {
        // given
        List<Employee> employees = Collections.singletonList(createEmployee());
        when(employeeRepository.findByFirstNameContainingIgnoreCase("Test")).thenReturn(employees);

        // when
        List<Employee> result = employeeService.getEmployeeByFirstName("Test");

        // then
        assertThat(result).hasSize(1);
        verify(employeeRepository).findByFirstNameContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("Should get employee by email")
    void shouldGetEmployeeByEmail() {
        // given
        Employee employee = createEmployee();
        when(employeeRepository.findByUserEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(employee));

        // when
        Employee result = employeeService.getEmployeeByEmail("test@example.com");

        // then
        assertThat(result).isNotNull();
        verify(employeeRepository).findByUserEmailIgnoreCase("test@example.com");
    }

    private EmployeeDto createEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("Test");
        dto.setLastName("Employee");
        dto.setEmail("test@example.com");
        dto.setPesel("12345678901");
        dto.setDepartmentId(1L);
        return dto;
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Test");
        employee.setLastName("Employee");
        employee.setPesel("12345678901");
        employee.setHireDate(LocalDate.now());

        User user = new User();
        user.setEmail("test@example.com");
        employee.setUser(user);

        return employee;
    }
}