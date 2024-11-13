package pl.krywion.usosremastered.audit;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JpaAuditingListener {

    private final ObjectFactory<AuditService> auditServiceProvider;

    public JpaAuditingListener(ObjectFactory<AuditService> auditServiceProvider) {
        this.auditServiceProvider = auditServiceProvider;
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        if (entity instanceof Identifiable) {
            auditServiceProvider.getObject().logAuditEvent(entity, AuditAction.CREATE);
        }
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        if (entity instanceof Identifiable) {
            auditServiceProvider.getObject().logAuditEvent(entity, AuditAction.UPDATE);

        }
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        if (entity instanceof Identifiable) {
            auditServiceProvider.getObject().logAuditEvent(entity, AuditAction.DELETE);
        }
    }
}
