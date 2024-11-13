package pl.krywion.usosremastered.service.employee.query;

import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface EmployeeQueryService {
    ServiceResponse<List<EmployeeDto>> findAll();
    ServiceResponse<EmployeeDto> findByPesel(String pesel);
    ServiceResponse<EmployeeDto> findByEmail(String email);
    ServiceResponse<List<EmployeeDto>> findByLastName(String lastName);
    ServiceResponse<List<EmployeeDto>> findByFirstName(String firstName);
}
