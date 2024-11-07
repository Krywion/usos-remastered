package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.krywion.usosremastered.entity.Institute;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {
}
