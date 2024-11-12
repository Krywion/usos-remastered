package pl.krywion.usosremastered.service.employee;


import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface EmployeeService {

    ServiceResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto);

    ServiceResponse<List<EmployeeDto>> getAllEmployees();

    ServiceResponse<EmployeeDto> updateEmployee(String pesel, EmployeeDto employeeDto);

    ServiceResponse<EmployeeDto> deleteEmployee(String pesel);

    ServiceResponse<EmployeeDto> getEmployeeByEmail(String email);

    ServiceResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName);

    ServiceResponse<EmployeeDto> getEmployeeByPesel(String pesel);

    ServiceResponse<List<EmployeeDto>> getEmployeeByFirstName(String firstName);
}
