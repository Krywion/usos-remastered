package pl.krywion.usosremastered.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.service.StudentService;
import pl.krywion.usosremastered.service.student.command.*;
import pl.krywion.usosremastered.service.student.query.StudentQueryService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentCommandHandler studentCommandHandler;
    private final StudentQueryService studentQueryService;

    @Override
    public Student createStudent(StudentDto studentDto) {
        return studentCommandHandler.handle(new CreateStudentCommand(studentDto));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentQueryService.findAll();
    }

    @Override
    public Student updateStudent(Long albumNumber, StudentDto studentDto) {
        return studentCommandHandler.handle(new UpdateStudentCommand(albumNumber, studentDto));
    }

    @Override
    public Student deleteStudent(Long albumNumber) {
        return studentCommandHandler.handle(new DeleteStudentCommand(albumNumber));
    }

    @Override
    public Student getStudentByAlbumNumber(Long albumNumber) {
        return studentQueryService.findByAlbumNumber(albumNumber);
    }

    @Override
    public Student getStudentByEmail(String email) {
        return studentQueryService.findByEmail(email);
    }

    @Override
    public List<Student> getStudentsByLastName(String lastName) {
        return studentQueryService.findByLastName(lastName);
    }

    @Override
    public List<Student> getStudentsByFirstName(String firstName) {
        return studentQueryService.findByFirstName(firstName);
    }

    @Override
    public Student assignToStudyPlan(Long albumNumber, Long studyPlanId) {
        return studentCommandHandler.handle(new AssignStudyPlanCommand(albumNumber, studyPlanId));
    }

    @Override
    public Student removeFromStudyPlan(Long albumNumber, Long studyPlanId) {
        return studentCommandHandler.handle(new RemoveFromStudyPlanCommand(albumNumber, studyPlanId));
    }

}
