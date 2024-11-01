package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.krywion.usosremastered.entity.Student;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserEmailIgnoreCase(String email);

    Collection<Student> findByLastNameIgnoreCase(String lastName);
}
