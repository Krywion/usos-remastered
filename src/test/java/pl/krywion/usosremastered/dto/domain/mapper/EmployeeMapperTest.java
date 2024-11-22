package pl.krywion.usosremastered.dto.domain.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Course;
import pl.krywion.usosremastered.entity.Department;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Mapper Tests")
class EmployeeMapperTest {
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    @DisplayName("Should map Employee to EmployeeDto")
    void shouldMapEmployeeToDto() {
        Employee employee = createEmployee();
        EmployeeDto dto = employeeMapper.toDto(employee);
        assertEmployeeDtoFields(dto, employee);
    }

    @Test
    @DisplayName("Should map EmployeeDto to Employee")
    void shouldMapDtoToEmployee() {
        EmployeeDto dto = createEmployeeDto();
        Employee employee = employeeMapper.toEntity(dto);
        assertEmployeeFields(employee, dto);
    }

    @Test
    @DisplayName("Should map list of Employees to list of EmployeeDtos")
    void shouldMapEmployeeListToDtoList() {
        List<Employee> employees = Arrays.asList(createEmployee(), createEmployee());
        List<EmployeeDto> dtos = employeeMapper.toDtoList(employees);
        assertEquals(2, dtos.size());
        assertEmployeeDtoFields(dtos.get(0), employees.get(0));
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setPesel("12345678901");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        User user = new User();
        user.setEmail("john.doe@example.com");
        employee.setUser(user);
        Department department = new Department();
        department.setId(1L);
        department.setName("IT Department");
        employee.setDepartment(department);
        Course course = new Course();
        course.setId(1L);
        employee.setCourses(List.of(course));
        return employee;
    }

    private EmployeeDto createEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setPesel("12345678901");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setDepartmentId(1L);
        dto.setCourseIds(List.of(1L));
        return dto;
    }

    private void assertEmployeeDtoFields(EmployeeDto dto, Employee employee) {
        assertEquals(employee.getPesel(), dto.getPesel());
        assertEquals(employee.getFirstName(), dto.getFirstName());
        assertEquals(employee.getLastName(), dto.getLastName());
        assertEquals(employee.getEmail(), dto.getEmail());
        assertEquals(employee.getDepartment().getId(), dto.getDepartmentId());
        assertEquals(employee.getDepartment().getName(), dto.getDepartmentName());
        assertEquals(employee.getCourses().get(0).getId(), dto.getCourseIds().get(0));
    }

    private void assertEmployeeFields(Employee employee, EmployeeDto dto) {
        assertEquals(dto.getPesel(), employee.getPesel());
        assertEquals(dto.getFirstName(), employee.getFirstName());
        assertEquals(dto.getLastName(), employee.getLastName());
    }
}