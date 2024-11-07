package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final StudentDtoValidator studentValidator;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudyPlanRepository studyPlanRepository,
            AuthenticationService authenticationService,
            UserRepository userRepository,
            StudentDtoValidator studentValidator,
            ModelMapper modelMapper
    ) {
        this.studentRepository = studentRepository;
        this.studyPlanRepository = studyPlanRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.studentValidator = studentValidator;
        this.modelMapper = modelMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<StudentDto> createStudent(StudentDto studentDto) {
        try {
            studentValidator.validate(studentDto);

            Student student = modelMapper.map(studentDto, Student.class);

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
            StudentDto createdStudentDto = modelMapper.map(savedStudent, StudentDto.class);

            log.info("Student created successfully: {}", createdStudentDto);

            return ApiResponse.success(
                    createdStudentDto,
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

    public ApiResponse<StudentDto> getStudentByAlbumNumber(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Student with album number %d not found", albumNumber)
        ));

        return ApiResponse.success(
                modelMapper.map(student, StudentDto.class),
                "Student found successfully",
                HttpStatus.OK
        );
    }

    public ApiResponse<StudentDto> getStudentByEmail(String email) {
        Student student = studentRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with email %s not found", email)
                ));

        return ApiResponse.success(
                modelMapper.map(student, StudentDto.class),
                "Student found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ApiResponse<List<StudentDto>> getStudentsByLastName(String lastName) {
        List<StudentDto> students = studentRepository.findByLastNameIgnoreCase(lastName)
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .toList();

        if(students.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("No students found with last name: %s", lastName)
            );
        }

        return ApiResponse.success(
                students,
                "Students found successfully",
                HttpStatus.OK
        );
    }


    @Override
    public ApiResponse<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentRepository.findAll()
                .stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());

        return ApiResponse.success(
                students,
                "All students retrieved successfully",
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<StudentDto> deleteStudent(Long albumNumber) {
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

        return ApiResponse.success(
                modelMapper.map(student, StudentDto.class),
                "Student deleted successfully",
                HttpStatus.OK
        );
    }



}
