package pl.krywion.usosremastered.audit;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuditEvent {
    private final String entityType;
    private final Long entityId;
    private final AuditAction action;
    private final String field;
    private final String oldValue;
    private final String newValue;
    private final String modifiedBy;
}
