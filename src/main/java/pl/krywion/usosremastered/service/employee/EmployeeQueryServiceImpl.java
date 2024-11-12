package pl.krywion.usosremastered.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public ServiceResponse<List<EmployeeDto>> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<EmployeeDto> findByPesel(String pesel) {
        Employee employee = employeeRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return ServiceResponse.success(
                employeeMapper.toDto(employee),
                "Employee found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<EmployeeDto> findByEmail(String email) {
        Employee employee = employeeRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return ServiceResponse.success(
                employeeMapper.toDto(employee),
                "Employee found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> findByLastName(String lastName) {
        List<Employee> employees = employeeRepository.findByLastNameContainingIgnoreCase(lastName);
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> findByFirstName(String firstName) {
        List<Employee> employees = employeeRepository.findByFirstNameContainingIgnoreCase(firstName);
        return ServiceResponse.success(
                employeeMapper.toDtoList(employees),
                "Employees found successfully",
                HttpStatus.OK
        );
    }
}
