package pl.krywion.usosremastered.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.StudentService;

import java.util.List;

// TODO 1: refactor the get methods to search for entities that contain the search term for:
//  - last name
//  - first name

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Student Management", description = "Operations pertaining to students in the University System")
public class StudentsController {

    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Create a new student",
            description = "Creates a new student with the provided details. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = StudentDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @PostMapping
    public ResponseEntity<ServiceResponse<StudentDto>> createStudent(@Valid @RequestBody StudentDto studentDto) {
        ServiceResponse<StudentDto> response = studentService.createStudent(studentDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get student by album number",
            description = "Retrieves a student using their unique album number"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping("/{albumNumber}")
    public ResponseEntity<ServiceResponse<StudentDto>> getStudentByAlbumNumber(@PathVariable Long albumNumber) {
        ServiceResponse<StudentDto> response = studentService.getStudentByAlbumNumber(albumNumber);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Find students by last name",
            description = "Retrieves all students with the specified last name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students found",
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(responseCode = "404", description = "No students found with given last name"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping("/by-lastname")
    public ResponseEntity<ServiceResponse<List<StudentDto>>> getStudentsByLastName(@RequestParam String lastname) {
        ServiceResponse<List<StudentDto>> response = studentService.getStudentsByLastName(lastname);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Find student by email",
            description = "Retrieves a student using their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping("/by-email")
    public ResponseEntity<ServiceResponse<StudentDto>> getStudentByEmail(@RequestParam String email) {
        ServiceResponse<StudentDto> response = studentService.getStudentByEmail(email);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieves a list of all students in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of students",
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping
    public ResponseEntity<ServiceResponse<List<StudentDto>>>getAllStudents() {
        ServiceResponse<List<StudentDto>> response = studentService.getAllStudents();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Delete a student",
            description = "Deletes a student with the specified album number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @DeleteMapping("/{albumNumber}")
    public ResponseEntity<ServiceResponse<StudentDto>> deleteStudent(@PathVariable Long albumNumber) {
        ServiceResponse<StudentDto> response = studentService.deleteStudent(albumNumber);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Update student details",
            description = "Updates the details of an existing student"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
    })
    @PutMapping("/{albumNumber}")
    public ResponseEntity<ServiceResponse<StudentDto>> updateStudent(@PathVariable Long albumNumber, @Valid @RequestBody StudentDto studentDto) {
        ServiceResponse<StudentDto> response = studentService.updateStudent(albumNumber, studentDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Find students by first name",
            description = "Retrieves all students with the specified first name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students found",
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(responseCode = "404", description = "No students found with given first name"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping("/by-firstname")
    public ResponseEntity<ServiceResponse<List<StudentDto>>> getStudentsByFirstName(@RequestParam String firstName) {
        ServiceResponse<List<StudentDto>> response = studentService.getStudentsByFirstName(firstName);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @Operation(
            summary = "Search students by full name",
            description = "Retrieves all students matching both first name and last name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Students found",
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(responseCode = "404", description = "No students found matching criteria"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @GetMapping("/search")
    public ResponseEntity<ServiceResponse<List<StudentDto>>> getStudentsByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        ServiceResponse<List<StudentDto>> response = studentService.getStudentsByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
