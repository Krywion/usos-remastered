package pl.krywion.usosremastered.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.EmployeeService;
import pl.krywion.usosremastered.service.employee.command.CreateEmployeeCommand;
import pl.krywion.usosremastered.service.employee.command.DeleteEmployeeCommand;
import pl.krywion.usosremastered.service.employee.command.EmployeeCommandHandler;
import pl.krywion.usosremastered.service.employee.command.UpdateEmployeeCommand;
import pl.krywion.usosremastered.service.employee.query.EmployeeQueryService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeCommandHandler employeeCommandHandler;
    private final EmployeeQueryService employeeQueryService;

    @Override
    public ServiceResponse<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        return employeeCommandHandler.handle(new CreateEmployeeCommand(employeeDto));
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> getAllEmployees() {
        return employeeQueryService.findAll();
    }

    @Override
    public ServiceResponse<EmployeeDto> updateEmployee(String pesel, EmployeeDto employeeDto) {
        return employeeCommandHandler.handle(new UpdateEmployeeCommand(pesel, employeeDto));
    }

    @Override
    public ServiceResponse<EmployeeDto> deleteEmployee(String pesel) {
        return employeeCommandHandler.handle(new DeleteEmployeeCommand(pesel));
    }

    @Override
    public ServiceResponse<EmployeeDto> getEmployeeByEmail(String email) {
        return employeeQueryService.findByEmail(email);
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> getEmployeesByLastName(String lastName) {
        return employeeQueryService.findByLastName(lastName);
    }

    @Override
    public ServiceResponse<EmployeeDto> getEmployeeByPesel(String pesel) {
        return employeeQueryService.findByPesel(pesel);
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> getEmployeeByFirstName(String firstName) {
        return employeeQueryService.findByFirstName(firstName);
    }
}
