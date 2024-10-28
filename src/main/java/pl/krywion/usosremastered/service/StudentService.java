package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.StudentDto;

public interface StudentService {

    StudentDto createStudent(StudentDto studentDto);

    StudentDto getStudent(Long id);
}
