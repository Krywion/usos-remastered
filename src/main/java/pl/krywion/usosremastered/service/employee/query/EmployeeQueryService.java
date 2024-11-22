package pl.krywion.usosremastered.service.employee.query;

import pl.krywion.usosremastered.entity.Employee;

import java.util.List;

public interface EmployeeQueryService {
    List<Employee> findAll();
    Employee findByPesel(String pesel);
    Employee findByEmail(String email);
    List<Employee>  findByLastName(String lastName);
    List<Employee>  findByFirstName(String firstName);
}
