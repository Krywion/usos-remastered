package pl.krywion.usosremastered.service.employee.command;

import pl.krywion.usosremastered.dto.domain.EmployeeDto;

public record UpdateEmployeeCommand(String pesel, EmployeeDto employeeDto) {
}
