package pl.krywion.usosremastered.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(JpaAuditingListener.class)
@Getter
@Setter
public abstract class AuditableEntity implements Identifiable {
}
