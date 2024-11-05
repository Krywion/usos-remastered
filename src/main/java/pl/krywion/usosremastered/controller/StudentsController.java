package pl.krywion.usosremastered.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentResponseDto;
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
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentResponseDto response = studentService.createStudent(studentDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{albumNumber}")
    public ResponseEntity<StudentDto> getStudentByAlbumNumber(@PathVariable Long albumNumber) {
        StudentDto student = studentService.getStudentByAlbumNumber(albumNumber);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-lastname")
    public ResponseEntity<List<StudentDto>> getStudentsByLastName(@RequestParam String lastname) {
        List<StudentDto> students = studentService.getStudentsByLastName(lastname);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/by-email")
    public ResponseEntity<StudentDto> getStudentByEmail(@RequestParam String email) {
        StudentDto student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @DeleteMapping("/{albumNumber}")
    public ResponseEntity<StudentResponseDto> deleteStudent(@PathVariable Long albumNumber) {
        StudentResponseDto response = studentService.deleteStudent(albumNumber);
        return ResponseEntity.ok(response);
    }
}
