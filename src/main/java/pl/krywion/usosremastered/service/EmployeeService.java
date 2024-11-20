package pl.krywion.usosremastered.service;


import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(EmployeeDto employeeDto);

    List<Employee> getAllEmployees();

    Employee updateEmployee(String pesel, EmployeeDto employeeDto);

    Employee deleteEmployee(String pesel);

    Employee getEmployeeByEmail(String email);

    List<Employee> getEmployeesByLastName(String lastName);

    Employee getEmployeeByPesel(String pesel);

    List<Employee> getEmployeeByFirstName(String firstName);
}
