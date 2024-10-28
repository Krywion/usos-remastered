package pl.krywion.usosremastered.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.repository.StudentRepository;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.StudentService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public StudentDto createStudent(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        student.setStudyPlan(studyPlanRepository.findById(studentDto.getStudyPlanId()).orElseThrow(() -> new RuntimeException("Study plan not found")));

        RegisterUserDto registerUserDto = new RegisterUserDto();

        registerUserDto.setEmail(studentDto.getEmail());
        registerUserDto.setRole(Role.ROLE_STUDENT.name());

        System.out.println("Registering student: " + registerUserDto.getEmail());
        System.out.println("Registering student: " + registerUserDto.getRole());

        this.authenticationService.signUp(registerUserDto);

        User user = userRepository.findByEmail(registerUserDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        student.setUser(user);

        System.out.println(student);
        studentRepository.save(student);
        return modelMapper.map(student, StudentDto.class);
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

}
