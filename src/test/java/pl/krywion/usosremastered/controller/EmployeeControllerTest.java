 package pl.krywion.usosremastered.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.service.EmployeeService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Employee Controller Tests")
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDto employeeDto;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeDto = createEmployeeDto();
        employee = createEmployee();
    }

    @DisplayName("Should create employee")
    @Test
    void shouldCreateEmployee() {
        // given
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);

        // when
        ResponseEntity<ServiceResponse<EmployeeDto>> response = employeeController.createEmployee(employeeDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).isEqualTo(employeeDto);
    }

    @DisplayName("Should get all employees")
    @Test
    void shouldGetAllEmployees() {
        // given
        List<Employee> employees = Arrays.asList(employee, employee);
        List<EmployeeDto> employeeDtos = Arrays.asList(employeeDto, employeeDto);

        when(employeeService.getAllEmployees()).thenReturn(employees);
        when(employeeMapper.toDtoList(employees)).thenReturn(employeeDtos);

        // when
        ResponseEntity<ServiceResponse<List<EmployeeDto>>> response = employeeController.getAllEmployees();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2);
    }

    @DisplayName("Should update employee")
    @Test
    void shouldUpdateEmployee() {
        // given
        String pesel = "12345678901";
        when(employeeService.updateEmployee(anyString(), any(EmployeeDto.class))).thenReturn(employee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);

        // when
        ResponseEntity<ServiceResponse<EmployeeDto>> response = employeeController.updateEmployee(pesel, employeeDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(employeeDto);
        verify(employeeService).updateEmployee(pesel, employeeDto);
    }

    @DisplayName("Should delete employee")
    @Test
    void shouldDeleteEmployee() {
        // given
        String pesel = "12345678901";
        when(employeeService.deleteEmployee(pesel)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        // when
        ResponseEntity<ServiceResponse<EmployeeDto>> response = employeeController.deleteEmployee(pesel);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(employeeService).deleteEmployee(pesel);
    }

    @DisplayName("Should get employee by PESEL")
    @Test
    void shouldGetEmployeeByPesel() {
        // given
        String pesel = "12345678901";
        when(employeeService.getEmployeeByPesel(pesel)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        // when
        ResponseEntity<ServiceResponse<EmployeeDto>> response = employeeController.getEmployeeByPesel(pesel);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(employeeDto);
    }

    @DisplayName("Should get employee by first name")
    @Test
    void shouldGetEmployeeByFirstName() {
        // given
        String firstName = "John";
        List<Employee> employees = Collections.singletonList(employee);
        List<EmployeeDto> employeeDtos = Collections.singletonList(employeeDto);

        when(employeeService.getEmployeeByFirstName(firstName)).thenReturn(employees);
        when(employeeMapper.toDtoList(employees)).thenReturn(employeeDtos);

        // when
        ResponseEntity<ServiceResponse<List<EmployeeDto>>> response = employeeController.getEmployeeByFirstName(firstName);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).hasSize(1);
    }

    @DisplayName("Should get employees by last name")
    @Test
    void shouldGetEmployeesByLastName() {
        // given
        String lastName = "Doe";
        List<Employee> employees = Collections.singletonList(employee);
        List<EmployeeDto> employeeDtos = Collections.singletonList(employeeDto);

        when(employeeService.getEmployeesByLastName(lastName)).thenReturn(employees);
        when(employeeMapper.toDtoList(employees)).thenReturn(employeeDtos);

        // when
        ResponseEntity<ServiceResponse<List<EmployeeDto>>> response = employeeController.getEmployeesByLastName(lastName);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).hasSize(1);
    }

    @DisplayName("Should get employee by email")
    @Test
    void shouldGetEmployeeByEmail() {
        // given
        String email = "john.doe@example.com";
        when(employeeService.getEmployeeByEmail(email)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        // when
        ResponseEntity<ServiceResponse<EmployeeDto>> response = employeeController.getEmployeeByEmail(email);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(employeeDto);
    }

    @DisplayName("Should handle ResourceNotFoundException")
    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        String pesel = "nonexistent";
        when(employeeService.getEmployeeByPesel(pesel))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        // when/then
        assertThatThrownBy(() -> employeeController.getEmployeeByPesel(pesel))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found");
    }

    private EmployeeDto createEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setPesel("12345678901");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setDepartmentId(1L);
        dto.setHireDate(LocalDate.now());
        return dto;
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setPesel("12345678901");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setHireDate(LocalDate.now());
        return employee;
    }
}