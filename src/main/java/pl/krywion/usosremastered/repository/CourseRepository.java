package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krywion.usosremastered.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
