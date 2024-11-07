package pl.krywion.usosremastered.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.EmployeeDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;
import pl.krywion.usosremastered.service.EmployeeService;


@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        ApiResponse<EmployeeDto> response = employeeService.createEmployee(employeeDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

}
