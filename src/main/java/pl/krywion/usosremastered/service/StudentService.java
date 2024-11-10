package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;

import java.util.List;

public interface StudentService {

    ApiResponse<StudentDto> createStudent(StudentDto studentDto);

    ApiResponse<StudentDto> getStudentByAlbumNumber(Long albumNumber);

    ApiResponse<StudentDto> getStudentByEmail(String email);

    ApiResponse<List<StudentDto>> getStudentsByLastName(String lastName);

    ApiResponse<List<StudentDto>> getAllStudents();

    ApiResponse<StudentDto> deleteStudent(Long albumNumber);

    ApiResponse<StudentDto> updateStudent(Long albumNumber, StudentDto studentDto);
}
