package pl.krywion.usosremastered.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.service.impl.StudentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/students")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class StudentsController {

    private final StudentServiceImpl studentService;

    public StudentsController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) {
        StudentDto createdStudent = studentService.createStudent(studentDto);
        return ResponseEntity.created(null).body(createdStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            StudentDto student = studentService.getStudent(id);
            return ResponseEntity.ok(student);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Student not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }


    @GetMapping()
    public ResponseEntity<List<StudentDto>> allStudents() {
        List<StudentDto> students = studentService.allStudents();
        return ResponseEntity.ok(students);
    }
}
