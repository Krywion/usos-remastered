package pl.krywion.usosremastered.service.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentCreationResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.StudentCreationException;
import pl.krywion.usosremastered.exception.StudyPlanNotFoundException;
import pl.krywion.usosremastered.exception.UserNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final StudyPlanRepository studyPlanRepository;

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudyPlanRepository studyPlanRepository,
            AuthenticationService authenticationService,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.studentRepository = studentRepository;
        this.studyPlanRepository = studyPlanRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public StudentCreationResponse createStudent(StudentDto studentDto) {
        try {
            validateStudentDto(studentDto);

            Student student = modelMapper.map(studentDto, Student.class);

            StudyPlan studyPlan = studyPlanRepository.findById(studentDto.getStudyPlanId())
                    .orElseThrow(() -> new StudyPlanNotFoundException(studentDto.getStudyPlanId()));
            student.setStudyPlan(studyPlan);

            RegisterUserDto registerUserDto = new RegisterUserDto();

            registerUserDto.setEmail(studentDto.getEmail());
            registerUserDto.setRole(Role.STUDENT);

            this.authenticationService.signUp(registerUserDto);

            User user = userRepository.findByEmail(registerUserDto.getEmail()).orElseThrow(() -> new UserNotFoundException(registerUserDto.getEmail()));
            student.setUser(user);

            studentRepository.save(student);
            log.info("Student created: {}", student);
            return new StudentCreationResponse(modelMapper.map(student, StudentDto.class), "Student created", true);
        } catch (Exception e) {
            log.error("Validation error: {}", e.getMessage());
            throw new StudentCreationException("Could not create student: " + e.getMessage(), e);
        }
    }

    public StudentDto getStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if(studentOptional.isEmpty()) {
            System.out.println("Student not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }

        return modelMapper.map(studentOptional.get(), StudentDto.class);
    }


    public List<StudentDto> allStudents() {
        return studentRepository.findAll().stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());
    }

    private void validateStudentDto(StudentDto studentDto) {
        List<String> errors = new ArrayList<>();

        if (studentDto.getEmail() == null || !isValidEmail(studentDto.getEmail())) {
            errors.add("Email is required");
        }

        if (studentDto.getStudyPlanId() == null) {
            errors.add("Study plan is required");
        }

        if (userRepository.existsByEmail(studentDto.getEmail())) {
            errors.add("User with this email already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}
