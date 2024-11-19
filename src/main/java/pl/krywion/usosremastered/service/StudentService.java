package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(StudentDto studentDto);

    List<Student> getAllStudents();

    Student updateStudent(Long albumNumber, StudentDto studentDto);

    Student deleteStudent(Long albumNumber);

    Student getStudentByAlbumNumber(Long albumNumber);

    Student getStudentByEmail(String email);

    List<Student> getStudentsByLastName(String lastName);

    List<Student> getStudentsByFirstName(String firstName);

    Student assignToStudyPlan(Long albumNumber, Long studyPlanId);

    Student removeFromStudyPlan(Long albumNumber, Long studyPlanId);
}
