package pl.krywion.usosremastered.service.student.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentQueryServiceImpl implements StudentQueryService {
    private final StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findByAlbumNumber(Long albumNumber) {
        return studentRepository.findById(albumNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with album number %d not found", albumNumber)
                ));
    }

    @Override
    public List<Student> findByLastName(String lastName) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        return studentRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Override
    public Student findByEmail(String email) {
        return studentRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with email %s not found", email)
                ));
    }
}
