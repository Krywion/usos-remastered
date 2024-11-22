package pl.krywion.usosremastered.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Employee;
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
    public Employee createEmployee(EmployeeDto employeeDto) {
        return employeeCommandHandler.handle(new CreateEmployeeCommand(employeeDto));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeQueryService.findAll();
    }

    @Override
    public Employee updateEmployee(String pesel, EmployeeDto employeeDto) {
        return employeeCommandHandler.handle(new UpdateEmployeeCommand(pesel, employeeDto));
    }

    @Override
    public Employee deleteEmployee(String pesel) {
        return employeeCommandHandler.handle(new DeleteEmployeeCommand(pesel));
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeQueryService.findByEmail(email);
    }

    @Override
    public List<Employee> getEmployeesByLastName(String lastName) {
        return employeeQueryService.findByLastName(lastName);
    }

    @Override
    public Employee getEmployeeByPesel(String pesel) {
        return employeeQueryService.findByPesel(pesel);
    }

    @Override
    public List<Employee> getEmployeeByFirstName(String firstName) {
        return employeeQueryService.findByFirstName(firstName);
    }
}
