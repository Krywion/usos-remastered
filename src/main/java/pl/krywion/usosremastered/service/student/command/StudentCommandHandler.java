package pl.krywion.usosremastered.service.student.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.EntityValidationException;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.service.StudyPlanService;
import pl.krywion.usosremastered.service.UserService;
import pl.krywion.usosremastered.validation.dto.StudentDtoValidator;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentCommandHandler {

    private final StudentRepository studentRepository;
    private final StudyPlanService studyPlanService;
    private final UserService userService;
    private final StudentDtoValidator validator;
    private final StudentMapper mapper;

    public ServiceResponse<StudentDto> handle(CreateStudentCommand command) {
        try {
            validator.validate(command.studentDto());

            Student student = mapper.toEntity(command.studentDto());
            setupStudentRelations(student, command.studentDto());

            Student savedStudent = studentRepository.save(student);

            return ServiceResponse.success(
                    mapper.toDto(savedStudent),
                    "Student created successfully",
                    HttpStatus.CREATED
            );
        } catch (EntityValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating student", e);
            throw new RuntimeException("Error creating student: " + e.getMessage());
        }
    }

    public ServiceResponse<StudentDto> handle(UpdateStudentCommand command) {
        try {
        validator.validateForUpdate(command.studentDto(), command.albumNumber());

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
        } catch (EntityValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating student", e);
            throw new RuntimeException("Error updating student: " + e.getMessage());
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

    public ServiceResponse<StudentDto> handle(AssignStudyPlanCommand command) {
        Student student = studentRepository.findById(command.albumNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", command.albumNumber())
                ));

        StudyPlan studyPlan = studyPlanService.getStudyPlan(command.studyPlanId());

        student.getStudyPlans().add(studyPlan);
        Student updatedStudent = studentRepository.save(student);

        return ServiceResponse.success(
                mapper.toDto(updatedStudent),
                "Study plan assigned successfully",
                HttpStatus.OK
        );
    }

    public ServiceResponse<StudentDto> handle(RemoveFromStudyPlanCommand command) {
        Student student = studentRepository.findById(command.albumNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", command.albumNumber())
                ));

        StudyPlan studyPlan = studyPlanService.getStudyPlan(command.studyPlanId());

        student.getStudyPlans().remove(studyPlan);
        Student updatedStudent = studentRepository.save(student);

        return ServiceResponse.success(
                mapper.toDto(updatedStudent),
                "Student removed from study plan successfully",
                HttpStatus.OK
        );
    }

    private void setupStudentRelations(Student student, StudentDto dto) {
        if (dto.getStudyPlanIds() != null && !dto.getStudyPlanIds().isEmpty()) {
            Set<StudyPlan> studyPlans = dto.getStudyPlanIds().stream()
                    .map(studyPlanService::getStudyPlan)
                    .collect(Collectors.toSet());
            student.setStudyPlans(studyPlans);
        }

        User user = userService.createUserForStudent(dto.getEmail());
        student.setUser(user);
    }

    private void updateStudentData(Student student, StudentDto dto) {
        try {
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());

            if (dto.getStudyPlanIds() != null) {
                Set<StudyPlan> newStudyPlans = dto.getStudyPlanIds().stream()
                        .map(studyPlanService::getStudyPlan)
                        .collect(Collectors.toSet());
                student.setStudyPlans(newStudyPlans);
            }

            if (!student.getUser().getEmail().equals(dto.getEmail())) {
                User user = userService.updateUserEmail(student.getUser().getEmail(), dto.getEmail());
                student.setUser(user);
            }
        } catch (Exception e) {
            log.error("Error updating student data", e);
            throw new ValidationException("Error updating student data: " + e.getMessage());
        }
    }
}
