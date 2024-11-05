package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.dto.response.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeByEmail(String email);

    List<EmployeeDto> getEmployeesByLastName(String lastName);

    List<EmployeeDto> getAllEmployees();

    EmployeeResponseDto deleteEmployee(Long employeeId);
}
