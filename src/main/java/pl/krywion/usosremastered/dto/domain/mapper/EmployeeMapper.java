package pl.krywion.usosremastered.dto.domain.mapper;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Course;
import pl.krywion.usosremastered.entity.Employee;

import java.util.List;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPesel(employee.getPesel());

        if(employee.getDepartment() != null) {
            employeeDto.setDepartmentId(employee.getDepartment().getId());
            employeeDto.setDepartmentName(employee.getDepartment().getName());
        }

        if(employee.getCourses() != null) {
            employeeDto.setCourseIds(employee.getCourses().stream().map(Course::getId).toList());
        }

        return employeeDto;
    }

    public List<EmployeeDto> toDtoList(List<Employee> employees) {
        if(employees == null) {
            return List.of();
        }

        return employees.stream()
                .map(this::toDto)
                .toList();
    }

    public Employee toEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setPesel(employeeDto.getPesel());

        return employee;
    }
}
