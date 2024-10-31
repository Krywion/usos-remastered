package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentCreationResponse;

public interface StudentService {

    StudentCreationResponse createStudent(StudentDto studentDto);

    StudentDto getStudent(Long id);
}
