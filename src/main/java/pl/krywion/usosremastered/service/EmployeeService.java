package pl.krywion.usosremastered.service;


import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;

import java.util.List;

public interface EmployeeService {
    ApiResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto);

    ApiResponse<EmployeeDto> getEmployeeByEmail(String email);

    ApiResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName);

    ApiResponse<List<EmployeeDto>> getAllEmployees();

    ApiResponse<EmployeeDto> deleteEmployee(Long employeeId);
}
