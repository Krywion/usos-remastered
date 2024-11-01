package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse createStudent(StudentDto studentDto);

    StudentDto getStudentByAlbumNumber(Long albumNumber);

    StudentDto getStudentByEmail(String email);

    List<StudentDto> getStudentsByLastName(String lastName);

    List<StudentDto> getAllStudents();

    StudentResponse deleteStudent(Long albumNumber);
}
