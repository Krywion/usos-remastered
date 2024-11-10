package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.krywion.usosremastered.entity.Student;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserEmailIgnoreCase(String email);

    List<Student> findByLastNameIgnoreCase(String lastName);

    List<Student> findByFirstNameIgnoreCase(String firstName);
    List<Student> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
