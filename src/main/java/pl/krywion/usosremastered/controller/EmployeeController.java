package pl.krywion.usosremastered.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.dto.response.EmployeeResponseDto;
import pl.krywion.usosremastered.service.EmployeeService;


@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeResponseDto response = employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok(response);
    }

}
