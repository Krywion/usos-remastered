package pl.krywion.usosremastered.service.student.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.service.UserService;
import pl.krywion.usosremastered.validation.dto.StudentDtoValidator;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentCommandHandler {

    private final StudentRepository studentRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final UserService userService;
    private final StudentDtoValidator validator;
    private final StudentMapper mapper;

    public ServiceResponse<StudentDto> handle(CreateStudentCommand command) {
        validator.validate(command.studentDto());
        try {
            Student student = mapper.toEntity(command.studentDto());
            setupStudentRelations(student, command.studentDto());

            Student savedStudent = studentRepository.save(student);

            return ServiceResponse.success(
                    mapper.toDto(savedStudent),
                    "Student created successfully",
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            log.error("Error creating student", e);
            if(e instanceof ValidationException) {
                log.error("Validation error creating student", e);
                throw e;
            } else {
                log.error("Error creating student", e);
                throw new RuntimeException("RuntimeException Error creating student: " + e.getMessage());
            }
        }
    }

    public ServiceResponse<StudentDto> handle(UpdateStudentCommand command) {
        try {
        validator.validateForUpdate(command.studentDto());

        Student student = studentRepository.findById(command.albumNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", command.albumNumber())
                ));

        updateStudentData(student, command.studentDto());
        Student updatedStudent = studentRepository.save(student);

        return ServiceResponse.success(
                mapper.toDto(updatedStudent),
                "Student updated successfully",
                HttpStatus.OK
        );
        } catch (Exception e) {
            log.error("Error updating student", e);
            if(e instanceof ValidationException || e instanceof ResourceNotFoundException) {
                throw e;
            }
            throw new ValidationException("Error updating student: " + e.getMessage());
        }
    }

    public ServiceResponse<StudentDto> handle(DeleteStudentCommand command) {
        Student student = studentRepository.findById(command.albumNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", command.albumNumber())
                ));

        userService.deleteUser(student.getUser().getEmail());
        studentRepository.delete(student);

        return ServiceResponse.success(
                mapper.toDto(student),
                "Student deleted successfully",
                HttpStatus.OK
        );
    }

    private void setupStudentRelations(Student student, StudentDto dto) {
        StudyPlan studyPlan = studyPlanRepository.findById(dto.getStudyPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Study plan with ID %d not found", dto.getStudyPlanId())
                ));
        student.setStudyPlan(studyPlan);

        User user = userService.createUser(dto.getEmail(), Role.STUDENT);
        student.setUser(user);
    }

    private void updateStudentData(Student student, StudentDto dto) {
        try {
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());

            if(!student.getStudyPlan().getId().equals(dto.getStudyPlanId())) {
                StudyPlan studyPlan = studyPlanRepository.findById(dto.getStudyPlanId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("Study plan with ID %d not found", dto.getStudyPlanId())
                        ));
                student.setStudyPlan(studyPlan);
            }

            if(!student.getUser().getEmail().equals(dto.getEmail())) {
                User user = userService.updateUser(dto.getEmail(), Role.STUDENT);
                student.setUser(user);
            }

        } catch (Exception e) {
            log.error("Error updating student data", e);
            throw new ValidationException("Error updating student data: " + e.getMessage());
        }

    }

}
