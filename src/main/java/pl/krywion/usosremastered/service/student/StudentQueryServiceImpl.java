package pl.krywion.usosremastered.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudentMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentQueryServiceImpl implements StudentQueryService {
    private final StudentRepository studentRepository;
    private final StudentMapper mapper;


    @Override
    public ServiceResponse<List<StudentDto>> findAll() {
        List<Student> students = studentRepository.findAll();

        return ServiceResponse.success(
                mapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<StudentDto> findByAlbumNumber(Long albumNumber) {
        Student student = studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", albumNumber)
                ));

        return ServiceResponse.success(
                mapper.toDto(student),
                "Student found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<StudentDto>> findByLastName(String lastName) {
        List<Student> students = studentRepository.findByLastNameContainingIgnoreCase(lastName);

        return ServiceResponse.success(
                mapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<List<StudentDto>> findByFirstName(String firstName) {
        List<Student> students = studentRepository.findByFirstNameContainingIgnoreCase(firstName);

        return ServiceResponse.success(
                mapper.toDtoList(students),
                "Students found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<StudentDto> findByEmail(String email) {
        Student student = studentRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with email %s not found", email)
                ));

        return ServiceResponse.success(
                mapper.toDto(student),
                "Student found successfully",
                HttpStatus.OK
        );
    }
}
