package pl.krywion.usosremastered.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;
import pl.krywion.usosremastered.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@PreAuthorize("hasRole('ADMIN')")
public class StudentsController {

    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDto>> createStudent(@Valid @RequestBody StudentDto studentDto) {
        ApiResponse<StudentDto> response = studentService.createStudent(studentDto);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/{albumNumber}")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentByAlbumNumber(@PathVariable Long albumNumber) {
        ApiResponse<StudentDto> response = studentService.getStudentByAlbumNumber(albumNumber);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/by-lastname")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getStudentsByLastName(@RequestParam String lastname) {
        ApiResponse<List<StudentDto>> response = studentService.getStudentsByLastName(lastname);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentByEmail(@RequestParam String email) {
        ApiResponse<StudentDto> response = studentService.getStudentByEmail(email);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDto>>>getAllStudents() {
        ApiResponse<List<StudentDto>> response = studentService.getAllStudents();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @DeleteMapping("/{albumNumber}")
    public ResponseEntity<ApiResponse<StudentDto>> deleteStudent(@PathVariable Long albumNumber) {
        ApiResponse<StudentDto> response = studentService.deleteStudent(albumNumber);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
