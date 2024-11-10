package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.exception.base.BaseException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.StudentService;
import pl.krywion.usosremastered.validation.dto.StudentDtoValidator;

import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final StudentDtoValidator studentValidator;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudyPlanRepository studyPlanRepository,
            AuthenticationService authenticationService,
            UserRepository userRepository,
            StudentDtoValidator studentValidator,
            StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studyPlanRepository = studyPlanRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.studentValidator = studentValidator;
        this.studentMapper = studentMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<StudentDto> createStudent(StudentDto studentDto) {
        try {
            studentValidator.validate(studentDto);

            Student student = studentMapper.toEntity(studentDto);

            StudyPlan studyPlan = studyPlanRepository.findById(studentDto.getStudyPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Study plan with ID %d not found", studentDto.getStudyPlanId())
                            ));
            student.setStudyPlan(studyPlan);

            RegisterUserDto registerUserDto = new RegisterUserDto();

            registerUserDto.setEmail(studentDto.getEmail());
            registerUserDto.setRole(Role.STUDENT);
            User user = authenticationService.signUp(registerUserDto);

            student.setUser(user);

            Student savedStudent = studentRepository.save(student);

            log.info("Student created successfully: {}", savedStudent);

            return ServiceResponse.success(
                    studentMapper.toDto(savedStudent),
                    "Student created successfully",
                    HttpStatus.CREATED
            );

        } catch (Exception e) {
            if(e instanceof BaseException) {
                throw e;
            }

            log.error("Failed to create student: ", e);
            throw new ValidationException("Failed to create student: " + e.getMessage());
        }
    }

    public ServiceResponse<StudentDto> getStudentByAlbumNumber(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Student with album number %d not found", albumNumber)
        ));

        return ServiceResponse.success(
                studentMapper.toDto(student),
                "Student found successfully",
                HttpStatus.OK
        );
    }

    public ServiceResponse<StudentDto> getStudentByEmail(String email) {
        Student student = studentRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with email %s not found", email)
                ));

        return ServiceResponse.success(
                studentMapper.toDto(student),
                "Student found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<StudentDto>> getStudentsByLastName(String lastName) {
        List<Student> students = studentRepository.findByLastNameIgnoreCase(lastName);

        return ServiceResponse.success(
                studentMapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }


    @Override
    public ServiceResponse<List<StudentDto>> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        return ServiceResponse.success(
                studentMapper.toDtoList(students),
                "All students retrieved successfully",
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<StudentDto> deleteStudent(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", albumNumber)
                ));

        User user = student.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        studentRepository.delete(student);
        log.info("Student deleted successfully: {}", student);

        return ServiceResponse.success(
                studentMapper.toDto(student),
                "Student deleted successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<StudentDto> updateStudent(Long albumNumber, StudentDto studentDto) {
        try {
            studentValidator.validateForUpdate(studentDto, albumNumber);

            Student student = studentRepository.findById(albumNumber)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Student with album number %d not found", albumNumber)
                    ));

            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());

            StudyPlan studyPlan = studyPlanRepository.findById(studentDto.getStudyPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Study plan with ID %d not found", studentDto.getStudyPlanId())
                    ));

            student.setStudyPlan(studyPlan);

            if(!student.getEmail().equals(studentDto.getEmail())) {
                User user = student.getUser();
                user.setEmail(studentDto.getEmail());
            }

            Student updatedStudent = studentRepository.save(student);
            log.info("Student updated successfully: {}", updatedStudent);

            return ServiceResponse.success(
                    studentMapper.toDto(updatedStudent),
                    "Student updated successfully",
                    HttpStatus.OK
            );
        } catch (Exception e) {
            if(e instanceof BaseException) {
                throw e;
            }

            log.error("Failed to update student: ", e);
            throw new ValidationException("Failed to update student: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse<List<StudentDto>> getStudentsByFirstName(String firstName) {
        List<Student> students = studentRepository.findByFirstNameIgnoreCase(firstName);

        if (students.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("No students found with first name: %s", firstName)
            );
        }

        return ServiceResponse.success(
                studentMapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<StudentDto>> getStudentsByFirstNameAndLastName(String firstName, String lastName) {
        List<Student> students = studentRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);

        if (students.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("No students found with first name: %s and last name: %s", firstName, lastName)
            );
        }

        return ServiceResponse.success(
                studentMapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }


}
