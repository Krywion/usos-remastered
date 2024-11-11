package pl.krywion.usosremastered.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.EmployeeService;

import java.util.List;


@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse<EmployeeDto>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        ServiceResponse<EmployeeDto> response = employeeService.createEmployee(employeeDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getAllEmployees() {
        ServiceResponse<List<EmployeeDto>> response = employeeService.getAllEmployees();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PutMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> updateEmployee(@PathVariable String pesel, @Valid @RequestBody EmployeeDto employeeDto) {
        ServiceResponse<EmployeeDto> response = employeeService.updateEmployee(pesel, employeeDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @DeleteMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> deleteEmployee(@PathVariable String pesel) {
        ServiceResponse<EmployeeDto> response = employeeService.deleteEmployee(pesel);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> getEmployeeByPesel(@PathVariable String pesel) {
        ServiceResponse<EmployeeDto> response = employeeService.getEmployeeByPesel(pesel);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/by-firstname")
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getEmployeeByFirstName(@RequestParam String firstName) {
        ServiceResponse<List<EmployeeDto>> response = employeeService.getEmployeeByFirstName(firstName);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/by-lastname")
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getEmployeesByLastName(@RequestParam String lastName) {
        ServiceResponse<List<EmployeeDto>> response = employeeService.getEmployeesByLastName(lastName);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<ServiceResponse<EmployeeDto>> getEmployeeByEmail(@RequestParam String email) {
        ServiceResponse<EmployeeDto> response = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }


}
