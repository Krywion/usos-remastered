package pl.krywion.usosremastered.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.krywion.usosremastered.entity.AuditLog;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
}
