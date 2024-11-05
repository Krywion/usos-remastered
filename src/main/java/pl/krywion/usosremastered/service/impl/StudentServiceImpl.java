package pl.krywion.usosremastered.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.dto.response.StudentResponseDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.StudentCreationException;
import pl.krywion.usosremastered.exception.StudentNotFoundException;
import pl.krywion.usosremastered.exception.StudyPlanNotFoundException;
import pl.krywion.usosremastered.exception.UserNotFoundException;
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
    public StudentResponseDto createStudent(StudentDto studentDto) {
        try {
            studentValidator.validate(studentDto);

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

            return new StudentResponseDto(modelMapper.map(student, StudentDto.class), "Student created", true);
        } catch (Exception e) {
            log.error("Validation error: {}", e.getMessage());
            throw new StudentCreationException("Could not create student: " + e.getMessage(), e);
        }
    }

    public StudentDto getStudentByAlbumNumber(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new StudentNotFoundException(albumNumber));

        return modelMapper.map(student, StudentDto.class);
    }

    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new StudentNotFoundException(email));

        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public List<StudentDto> getStudentsByLastName(String lastName) {
        return studentRepository.findByLastNameIgnoreCase(lastName).stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());
    }


    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentResponseDto deleteStudent(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber
        ).orElseThrow(() -> new StudentNotFoundException(albumNumber));

        User user = student.getUser();
        userRepository.delete(user);
        studentRepository.delete(student);

        log.info("Student deleted: {}", student);

        return new StudentResponseDto(modelMapper.map(student, StudentDto.class), "Student deleted", true);
    }



}
