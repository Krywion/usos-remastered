package pl.krywion.usosremastered.service.employee.command;

import pl.krywion.usosremastered.dto.domain.EmployeeDto;

public record CreateEmployeeCommand(EmployeeDto employeeDto) {
}
