package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krywion.usosremastered.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
