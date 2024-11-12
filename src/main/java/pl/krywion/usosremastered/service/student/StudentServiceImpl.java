package pl.krywion.usosremastered.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.student.command.CreateStudentCommand;
import pl.krywion.usosremastered.service.student.command.DeleteStudentCommand;
import pl.krywion.usosremastered.service.student.command.StudentCommandHandler;
import pl.krywion.usosremastered.service.student.command.UpdateStudentCommand;


import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentCommandHandler studentCommandHandler;
    private final StudentQueryService studentQueryService;

    @Override
    public ServiceResponse<StudentDto> createStudent(StudentDto studentDto) {
        return studentCommandHandler.handle(new CreateStudentCommand(studentDto));
    }

    @Override
    public ServiceResponse<List<StudentDto>> getAllStudents() {
        return studentQueryService.findAll();
    }

    @Override
    public ServiceResponse<StudentDto> updateStudent(Long albumNumber, StudentDto studentDto) {
        return studentCommandHandler.handle(new UpdateStudentCommand(albumNumber, studentDto));
    }

    @Override
    public ServiceResponse<StudentDto> deleteStudent(Long albumNumber) {
        return studentCommandHandler.handle(new DeleteStudentCommand(albumNumber));
    }

    @Override
    public ServiceResponse<StudentDto> getStudentByAlbumNumber(Long albumNumber) {
        return studentQueryService.findByAlbumNumber(albumNumber);
    }

    @Override
    public ServiceResponse<StudentDto> getStudentByEmail(String email) {
        return studentQueryService.findByEmail(email);
    }

    @Override
    public ServiceResponse<List<StudentDto>> getStudentsByLastName(String lastName) {
        return studentQueryService.findByLastName(lastName);
    }

    @Override
    public ServiceResponse<List<StudentDto>> getStudentsByFirstName(String firstName) {
        return studentQueryService.findByFirstName(firstName);
    }

}
