package pl.krywion.usosremastered.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Student Controller Tests")
@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentsController studentsController;

    private StudentDto studentDto;
    private Student student;

    @BeforeEach
    void setUp() {
        studentDto = createStudentDto();
        student = createStudent();
    }

    @DisplayName("Should create student")
    @Test
    void shouldCreateStudent() {
        // given
        when(studentService.createStudent(any(StudentDto.class))).thenReturn(student);
        when(studentMapper.toDto(any(Student.class))).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.createStudent(studentDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).isEqualTo(studentDto);
    }

    @DisplayName("Should get student by album number")
    @Test
    void shouldGetStudentByAlbumNumber() {
        // given
        Long albumNumber = 123L;
        when(studentService.getStudentByAlbumNumber(albumNumber)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.getStudentByAlbumNumber(albumNumber);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(studentDto);
    }

    @DisplayName("Should get students by last name")
    @Test
    void shouldGetStudentsByLastName() {
        // given
        String lastName = "Doe";
        List<Student> students = Collections.singletonList(student);
        List<StudentDto> studentDtos = Collections.singletonList(studentDto);

        when(studentService.getStudentsByLastName(lastName)).thenReturn(students);
        when(studentMapper.toDtoList(students)).thenReturn(studentDtos);

        // when
        ResponseEntity<ServiceResponse<List<StudentDto>>> response = studentsController.getStudentsByLastName(lastName);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).hasSize(1);
    }

    @DisplayName("Should get student by email")
    @Test
    void shouldGetStudentByEmail() {
        // given
        String email = "john.doe@example.com";
        when(studentService.getStudentByEmail(email)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.getStudentByEmail(email);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(studentDto);
    }

    @DisplayName("Should get all students")
    @Test
    void shouldGetAllStudents() {
        // given
        List<Student> students = Arrays.asList(student, student);
        List<StudentDto> studentDtos = Arrays.asList(studentDto, studentDto);

        when(studentService.getAllStudents()).thenReturn(students);
        when(studentMapper.toDtoList(students)).thenReturn(studentDtos);

        // when
        ResponseEntity<ServiceResponse<List<StudentDto>>> response = studentsController.getAllStudents();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).hasSize(2);
    }

    @DisplayName("Should delete student")
    @Test
    void shouldDeleteStudent() {
        // given
        Long albumNumber = 123L;
        when(studentService.deleteStudent(albumNumber)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.deleteStudent(albumNumber);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(studentService).deleteStudent(albumNumber);
    }

    @DisplayName("Should update student")
    @Test
    void shouldUpdateStudent() {
        // given
        Long albumNumber = 123L;
        when(studentService.updateStudent(anyLong(), any(StudentDto.class))).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.updateStudent(albumNumber, studentDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(studentDto);
    }

    @DisplayName("Should get students by first name")
    @Test
    void shouldGetStudentsByFirstName() {
        // given
        String firstName = "John";
        List<Student> students = Collections.singletonList(student);
        List<StudentDto> studentDtos = Collections.singletonList(studentDto);

        when(studentService.getStudentsByFirstName(firstName)).thenReturn(students);
        when(studentMapper.toDtoList(students)).thenReturn(studentDtos);

        // when
        ResponseEntity<ServiceResponse<List<StudentDto>>> response = studentsController.getStudentsByFirstName(firstName);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).hasSize(1);
    }

    @DisplayName("Should assign study plan")
    @Test
    void shouldAssignStudyPlan() {
        // given
        Long albumNumber = 123L;
        Long studyPlanId = 1L;
        when(studentService.assignToStudyPlan(albumNumber, studyPlanId)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.assignStudyPlan(albumNumber, studyPlanId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(studentService).assignToStudyPlan(albumNumber, studyPlanId);
    }

    @DisplayName("Should remove study plan")
    @Test
    void shouldRemoveStudyPlan() {
        // given
        Long albumNumber = 123L;
        Long studyPlanId = 1L;
        when(studentService.removeFromStudyPlan(albumNumber, studyPlanId)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // when
        ResponseEntity<ServiceResponse<StudentDto>> response = studentsController.removeStudyPlan(albumNumber, studyPlanId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(studentService).removeFromStudyPlan(albumNumber, studyPlanId);
    }

    @DisplayName("Should handle resource not found exception")
    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        Long nonExistentAlbumNumber = 999L;
        when(studentService.getStudentByAlbumNumber(nonExistentAlbumNumber))
                .thenThrow(new ResourceNotFoundException("Student not found"));

        // when/then
        assertThatThrownBy(() -> studentsController.getStudentByAlbumNumber(nonExistentAlbumNumber))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found");
    }

    private StudentDto createStudentDto() {
        StudentDto dto = new StudentDto();
        dto.setAlbumNumber(123L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setStudyPlanIds(Collections.singletonList(1L));
        return dto;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setAlbumNumber(123L);
        student.setFirstName("John");
        student.setLastName("Doe");
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(1L);
        student.getStudyPlans().add(studyPlan);
        return student;
    }
}
