package pl.krywion.usosremastered.dto.domain.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Student Mapper Tests")
public class StudentMapperTest {
    private final StudentMapper studentMapper = new StudentMapper();

    @Test
    @DisplayName("Should map student entity to student dto")
    void shouldMapStudentEntityToStudentDto() {
        // given
        Student student = createStudent();
        StudentDto dto = studentMapper.toDto(student);
        // when/then
        assertStudentDtoFields(dto, student);
    }

    @Test
    @DisplayName("Should map student dto to student entity")
    void shouldMapStudentDtoToStudentEntity() {
        // given
        StudentDto dto = createStudentDto();
        Student student = studentMapper.toEntity(dto);
        // when/then
        assertStudentFields(student, dto);
    }

    @Test
    @DisplayName("Should map list of student entities to list of student dtos")
    void shouldMapStudentListToDtoList() {
        // given
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.setAlbumNumber(2L);
        List<Student> students = Arrays.asList(student1, student2);
        // when
        List<StudentDto> dtos = studentMapper.toDtoList(students);
        // then
        assertEquals(students.size(), dtos.size());
        assertStudentDtoFields(dtos.get(0), student1);
        assertStudentDtoFields(dtos.get(1), student2);
    }

    private Student createStudent() {
        Student student = new Student();
        student.setAlbumNumber(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        User user = new User();
        user.setEmail("john.doe@example.com");
        student.setUser(user);
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(1L);
        student.setStudyPlans(new HashSet<>(List.of(studyPlan)));
        return student;
    }

    private StudentDto createStudentDto() {
        StudentDto dto = new StudentDto();
        dto.setAlbumNumber(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setStudyPlanIds(List.of(1L));
        return dto;
    }

    private void assertStudentDtoFields(StudentDto dto, Student student) {
        assertEquals(student.getAlbumNumber(), dto.getAlbumNumber());
        assertEquals(student.getFirstName(), dto.getFirstName());
        assertEquals(student.getLastName(), dto.getLastName());
        assertEquals(student.getEmail(), dto.getEmail());
        assertEquals(student.getStudyPlans().size(), dto.getStudyPlanIds().size());
        assertTrue(dto.getStudyPlanIds().contains(student.getStudyPlans().iterator().next().getId()));
    }

    private void assertStudentFields(Student student, StudentDto dto) {
        assertEquals(dto.getFirstName(), student.getFirstName());
        assertEquals(dto.getLastName(), student.getLastName());
    }
}
