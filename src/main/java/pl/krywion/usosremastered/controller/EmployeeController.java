package pl.krywion.usosremastered.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.krywion.usosremastered.dto.domain.EmployeeDto;
import pl.krywion.usosremastered.dto.domain.mapper.EmployeeMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.EmployeeService;

import java.util.List;

@Transactional
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/employee")
@Tag(name = "Employee Management", description = "Operations pertaining to employees in the University System")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee with the provided details. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<EmployeeDto>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        ServiceResponse<EmployeeDto> response = ServiceResponse.success(
                employeeMapper.toDto(employeeService.createEmployee(employeeDto)),
                "Employee created successfully",
                HttpStatus.CREATED
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get all employees",
            description = "Retrieves all employees"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getAllEmployees() {
        ServiceResponse<List<EmployeeDto>> response = ServiceResponse.success(
                employeeMapper.toDtoList(employeeService.getAllEmployees()),
                "Employees retrieved successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Update employee details",
            description = "Updates the details of an existing employee identified by PESEL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> updateEmployee(@PathVariable String pesel, @Valid @RequestBody EmployeeDto employeeDto) {
        ServiceResponse<EmployeeDto> response = ServiceResponse.success(
                employeeMapper.toDto(employeeService.updateEmployee(pesel, employeeDto)),
                "Employee updated successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Delete an employee",
            description = "Deletes an existing with the specified PESEL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee deleted successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> deleteEmployee(@PathVariable String pesel) {
        ServiceResponse<EmployeeDto> response = ServiceResponse.success(
                employeeMapper.toDto(employeeService.deleteEmployee(pesel)),
                "Employee deleted successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get employee by PESEL",
            description = "Retrieves an employee with the specified PESEL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{pesel}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> getEmployeeByPesel(@PathVariable String pesel) {
        ServiceResponse<EmployeeDto> response = ServiceResponse.success(
                employeeMapper.toDto(employeeService.getEmployeeByPesel(pesel)),
                "Employee retrieved successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get employees by first name",
            description = "Retrieves a list of employees with the specified first name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees found",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @GetMapping("/by-firstname")
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getEmployeeByFirstName(@RequestParam String firstName) {
        ServiceResponse<List<EmployeeDto>> response = ServiceResponse.success(
                employeeMapper.toDtoList(employeeService.getEmployeeByFirstName(firstName)),
                "Employees retrieved successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get employees by last name",
            description = "Retrieves a list of employees with the specified last name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees found",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @GetMapping("/by-lastname")
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getEmployeesByLastName(@RequestParam String lastName) {
        ServiceResponse<List<EmployeeDto>> response = ServiceResponse.success(
                employeeMapper.toDtoList(employeeService.getEmployeesByLastName(lastName)),
                "Employees retrieved successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get employee by email",
            description = "Retrieves an employee using their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @GetMapping("/by-email")
    public ResponseEntity<ServiceResponse<EmployeeDto>> getEmployeeByEmail(@RequestParam String email) {
        ServiceResponse<EmployeeDto> response = ServiceResponse.success(
                employeeMapper.toDto(employeeService.getEmployeeByEmail(email)),
                "Employee retrieved successfully",
                HttpStatus.OK
        );
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }


}
