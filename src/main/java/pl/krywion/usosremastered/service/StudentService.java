package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface StudentService {

    ServiceResponse<StudentDto> createStudent(StudentDto studentDto);

    ServiceResponse<List<StudentDto>> getAllStudents();

    ServiceResponse<StudentDto> updateStudent(Long albumNumber, StudentDto studentDto);

    ServiceResponse<StudentDto> deleteStudent(Long albumNumber);

    ServiceResponse<StudentDto> getStudentByAlbumNumber(Long albumNumber);

    ServiceResponse<StudentDto> getStudentByEmail(String email);

    ServiceResponse<List<StudentDto>> getStudentsByLastName(String lastName);

    ServiceResponse<List<StudentDto>> getStudentsByFirstName(String firstName);

    ServiceResponse<StudentDto> assignToStudyPlan(Long albumNumber, Long studyPlanId);

    ServiceResponse<StudentDto> removeFromStudyPlan(Long albumNumber, Long studyPlanId);
}
