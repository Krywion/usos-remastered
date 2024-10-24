package pl.krywion.usosremastered.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.entity.Employee;
import pl.krywion.usosremastered.service.EmployeeService;

import java.time.LocalDate;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public ResponseEntity<Employee> getEmployee() {
        return new ResponseEntity<>(employeeService.getEmployeeById(1L), HttpStatus.OK);
    }

    @GetMapping("/employee/save")
    public ResponseEntity<String> saveEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        LocalDate hireDate = LocalDate.now();
        employee.setHireDate(hireDate);
        employee.setPesel("12345678901");
        employeeService.saveEmployee(employee);
        return new ResponseEntity<>("Employee saved", HttpStatus.OK);
    }
}
