package pl.krywion.usosremastered.service.student.query;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.entity.Student;

import java.util.List;

@Service
public interface StudentQueryService {
    List<Student> findAll();
    Student findByAlbumNumber(Long albumNumber);
    List<Student> findByLastName(String lastName);
    List<Student> findByFirstName(String firstName);
    Student findByEmail(String email);
}
