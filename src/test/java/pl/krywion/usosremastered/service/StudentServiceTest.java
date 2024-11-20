package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.service.student.StudentServiceImpl;
import pl.krywion.usosremastered.service.student.command.StudentCommandHandler;
import pl.krywion.usosremastered.service.student.query.StudentQueryServiceImpl;
import pl.krywion.usosremastered.validation.dto.StudentDtoValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Student Service Tests")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserService userService;
    @Mock
    private StudyPlanService studyPlanService;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentDtoValidator validator;

    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(
                new StudentCommandHandler(studentRepository, studyPlanService, userService, validator, studentMapper),
                new StudentQueryServiceImpl(studentRepository)
        );
    }

    @DisplayName("Should create student")
    @Test
    void shouldCreateStudent() {
        // given
        StudentDto dto = createStudentDto();
        Student student = createStudent();
        User user = new User();
        user.setEmail("test@example.com");

        when(studentMapper.toEntity(dto)).thenReturn(student);
        when(userService.createUserForStudent(dto.getEmail())).thenReturn(user);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.createStudent(dto);

        // then
        assertThat(result).isNotNull();
        verify(validator).validate(dto);
        verify(studentRepository).save(any(Student.class));
        verify(userService).createUserForStudent(dto.getEmail());
    }

    @DisplayName("Should get all students")
    @Test
    void shouldGetAllStudents() {
        // given
        List<Student> students = Arrays.asList(createStudent(), createStudent());
        when(studentRepository.findAll()).thenReturn(students);

        // when
        List<Student> result = studentService.getAllStudents();

        // then
        assertThat(result).hasSize(2);
        verify(studentRepository).findAll();
    }

    @DisplayName("Should get student by album number")
    @Test
    void shouldGetStudentByAlbumNumber() {
        // given
        Student student = createStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // when
        Student result = studentService.getStudentByAlbumNumber(1L);

        // then
        assertThat(result).isNotNull();
        verify(studentRepository).findById(1L);
    }

    @DisplayName("Should throw exception when student not found")
    @Test
    void shouldThrowExceptionWhenStudentNotFound() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> studentService.getStudentByAlbumNumber(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Should update student")
    @Test
    void shouldUpdateStudent() {
        // given
        StudentDto dto = createStudentDto();
        Student existingStudent = createStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        // when
        Student result = studentService.updateStudent(1L, dto);

        // then
        assertThat(result).isNotNull();
        verify(validator).validateForUpdate(dto, 1L);
        verify(studentRepository).save(any(Student.class));
    }

    @DisplayName("Should delete student")
    @Test
    void shouldDeleteStudent() {
        // given
        Student student = createStudent();
        User user = new User();
        user.setEmail("test@example.com");
        student.setUser(user);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // when
        studentService.deleteStudent(1L);

        // then
        verify(studentRepository).delete(student);
        verify(userService).deleteUser(user.getEmail());
    }

    @DisplayName("Should assign study plan")
    @Test
    void shouldAssignStudyPlan() {
        // given
        Student student = createStudent();
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studyPlanService.getStudyPlan(1L)).thenReturn(studyPlan);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.assignToStudyPlan(1L, 1L);

        // then
        assertThat(result.getStudyPlans()).contains(studyPlan);
        verify(studentRepository).save(student);
    }

    @DisplayName("Should remove study plan")
    @Test
    void shouldRemoveStudyPlan() {
        // given
        Student student = createStudent();
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(1L);
        student.getStudyPlans().add(studyPlan);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studyPlanService.getStudyPlan(1L)).thenReturn(studyPlan);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = studentService.removeFromStudyPlan(1L, 1L);

        // then
        assertThat(result.getStudyPlans()).doesNotContain(studyPlan);
        verify(studentRepository).save(student);
    }

    @DisplayName("Should get students by study plan")
    @Test
    void shouldGetStudentsByLastName() {
        // given
        List<Student> students = Collections.singletonList(createStudent());
        when(studentRepository.findByLastNameContainingIgnoreCase("Test")).thenReturn(students);

        // when
        List<Student> result = studentService.getStudentsByLastName("Test");

        // then
        assertThat(result).hasSize(1);
        verify(studentRepository).findByLastNameContainingIgnoreCase("Test");
    }

    @DisplayName("Should get students by first name")
    @Test
    void shouldGetStudentsByFirstName() {
        // given
        List<Student> students = Collections.singletonList(createStudent());
        when(studentRepository.findByFirstNameContainingIgnoreCase("Test")).thenReturn(students);

        // when
        List<Student> result = studentService.getStudentsByFirstName("Test");

        // then
        assertThat(result).hasSize(1);
        verify(studentRepository).findByFirstNameContainingIgnoreCase("Test");
    }

    private StudentDto createStudentDto() {
        StudentDto dto = new StudentDto();
        dto.setFirstName("Test");
        dto.setLastName("Student");
        dto.setEmail("test@example.com");
        return dto;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("Student");
        User user = new User();
        user.setEmail("test@example.com");
        student.setUser(user);
        return student;
    }
}