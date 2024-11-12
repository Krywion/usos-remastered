package pl.krywion.usosremastered.service.student.command;

import pl.krywion.usosremastered.dto.domain.StudentDto;

public record UpdateStudentCommand(Long albumNumber, StudentDto studentDto) {
}
