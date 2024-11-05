package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentResponseDto;

import java.util.List;

public interface StudentService {

    StudentResponseDto createStudent(StudentDto studentDto);

    StudentDto getStudentByAlbumNumber(Long albumNumber);

    StudentDto getStudentByEmail(String email);

    List<StudentDto> getStudentsByLastName(String lastName);

    List<StudentDto> getAllStudents();

    StudentResponseDto deleteStudent(Long albumNumber);
}
