package pl.krywion.usosremastered.service;


import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface EmployeeService {
    ServiceResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto);

    ServiceResponse<EmployeeDto> getEmployeeByEmail(String email);

    ServiceResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName);

    ServiceResponse<List<EmployeeDto>> getAllEmployees();

    ServiceResponse<EmployeeDto> deleteEmployee(Long employeeId);
}
