package pl.krywion.usosremastered.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.util.UserContextProvider;

@Aspect
@Component
@Slf4j
public class AuditAspect {
    private final ApplicationEventPublisher eventPublisher;
    private final UserContextProvider userContextProvider;

    public AuditAspect(ApplicationEventPublisher eventPublisher, UserContextProvider userContextProvider) {
        this.eventPublisher = eventPublisher;
        this.userContextProvider = userContextProvider;
    }

    @AfterReturning(
            pointcut = "execution(* pl.krywion.usosremastered.service..*ServiceImpl.create*(..))",
            returning = "result"
    )
    public void auditCreate(Object result) {
        if ( result instanceof ServiceResponse) {
            Object data = ((ServiceResponse<?>) result).getData();
            publishAuditEvent(data, null, AuditAction.CREATE);
        }
    }

    @AfterReturning(
            pointcut = "execution(* pl.krywion.usosremastered.service..*ServiceImpl.update*(..))",
            returning = "result"
    )
    public void auditUpdate(Object result) {
        if ( result instanceof ServiceResponse) {
            Object data = ((ServiceResponse<?>) result).getData();

            publishAuditEvent(data, ((ServiceResponse<?>) result).getData().toString(), AuditAction.UPDATE);
        }
    }

    @AfterReturning(
            pointcut = "execution(* pl.krywion.usosremastered.service..*ServiceImpl.delete*(..))",
            returning = "result"
    )
    public void auditDelete(Object result) {
        if ( result instanceof ServiceResponse) {
            Object data = ((ServiceResponse<?>) result).getData();
            publishAuditEvent(data, null, AuditAction.DELETE);
        }
    }

    private void publishAuditEvent(Object entity, String oldValue, AuditAction action) {
        try {
            String username = userContextProvider.getCurrentUser();

            log.debug("Publishing audit event for entity: {}", entity.getClass().getSimpleName());

            AuditEvent event = AuditEvent.builder()
                    .entityType(entity.getClass().getSimpleName())
                    .entityId(getEntityId(entity))
                    .action(action)
                    .field("all")
                    .oldValue(oldValue)
                    .newValue(entity.toString())
                    .modifiedBy(username)
                    .build();

            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Error while publishing audit event", e);
        }

    }

    private Long getEntityId(Object entity) {
        try {
            if(entity instanceof Identifiable) {
                return ((Identifiable) entity).getIdentifier();
            }
            try {
                return (Long) entity.getClass().getMethod("getId").invoke(entity);
            } catch (Exception e) {
                log.error("Error while getting entity id", e);
                return null;
            }
        } catch (Exception e) {
            log.error("Error while getting entity id", e);
            return null;
        }
    }


}
