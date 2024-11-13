package pl.krywion.usosremastered.service.common;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.audit.AuditEvent;
import pl.krywion.usosremastered.entity.AuditLog;
import pl.krywion.usosremastered.repository.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void handleAuditEvent(AuditEvent auditEvent) {
        AuditLog auditLog = AuditLog.builder()
                .entityType(auditEvent.getEntityType())
                .entityId(auditEvent.getEntityId())
                .action(auditEvent.getAction().name())
                .field(auditEvent.getField())
                .oldValue(auditEvent.getOldValue())
                .newValue(auditEvent.getNewValue())
                .modifiedBy(auditEvent.getModifiedBy())
                .modifiedAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAuditLogsForEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
}
