package pl.krywion.usosremastered.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.krywion.usosremastered.audit.AuditAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "changes", columnDefinition = "TEXT")
    private String changes;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;
}
