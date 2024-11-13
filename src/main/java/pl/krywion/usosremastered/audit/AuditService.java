package pl.krywion.usosremastered.audit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.entity.AuditLog;
import pl.krywion.usosremastered.repository.AuditLogRepository;
import pl.krywion.usosremastered.util.EntitySerializer;
import pl.krywion.usosremastered.util.UserContextProvider;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final UserContextProvider userContextProvider;
    private final EntityManager entityManager;
    private final EntitySerializer entitySerializer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditEvent(Object entity, AuditAction action) {
        try {

            if (!(entity instanceof Identifiable identifiable)) {
                return;
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setEntityType(entity.getClass().getSimpleName());
            auditLog.setEntityId(identifiable.getIdentifier());
            auditLog.setAction(action);
            auditLog.setModifiedBy(userContextProvider.getCurrentUser());
            auditLog.setModifiedAt(LocalDateTime.now());

            switch (action) {
                case CREATE -> auditLog.setNewValue(entitySerializer.serializeForAudit(entity));
                case UPDATE -> {
                    Object originalState = entityManager.find(entity.getClass(), identifiable.getIdentifier());
                    auditLog.setOldValue(entitySerializer.serializeForAudit(originalState));
                    auditLog.setNewValue(entitySerializer.serializeForAudit(entity));
                    auditLog.setChanges(generateChangesJson(originalState, entity));
                }
                case DELETE -> auditLog.setOldValue(entitySerializer.serializeForAudit(entity));
            }

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error during audit logging", e);
        }
    }

    private String generateChangesJson(Object oldValue, Object newValue) {
        try {
            return entitySerializer.serializeForAudit(compareObjects(oldValue, newValue));
        } catch (Exception e) {
            log.error("Error generating changes JSON", e);
            return "{}";
        }
    }

    private Map<String, Object> compareObjects(Object oldValue, Object newValue) {
        if (oldValue == null || newValue == null) {
            return Map.of("status", "Object was created or deleted");
        }

        Map<String, Object> changes = new HashMap<>();

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(oldValue.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null &&
                        !pd.getName().equals("class") &&
                        !pd.getName().equals("hibernateLazyInitializer") &&
                        !pd.getName().equals("handler")) {

                    Object oldVal = pd.getReadMethod().invoke(oldValue);
                    Object newVal = pd.getReadMethod().invoke(newValue);

                    if (!objectsEqual(oldVal, newVal)) {
                        changes.put(pd.getName(), Map.of(
                                "old", oldVal != null ? oldVal.toString() : "null",
                                "new", newVal != null ? newVal.toString() : "null"
                        ));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error comparing objects", e);
        }

        return changes;
    }

    private boolean objectsEqual(Object obj1, Object obj2) {
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null) return false;

        if (obj1 instanceof Identifiable && obj2 instanceof Identifiable) {
            return ((Identifiable) obj1).getIdentifier().equals(((Identifiable) obj2).getIdentifier());
        }

        return obj1.equals(obj2);
    }
}
