package pl.krywion.usosremastered.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.audit.Identifiable;

@Component
@RequiredArgsConstructor
public class EntitySerializer {
    private final ObjectMapper objectMapper;

    public String serializeForAudit(Object entity) {
        try {
            if (entity instanceof Identifiable identifiable) {
                ObjectNode node = objectMapper.createObjectNode();
                node.put("id", identifiable.getIdentifier());
                node.put("type", entity.getClass().getSimpleName());

                ObjectNode properties = objectMapper.valueToTree(entity);
                properties.remove("institutes");
                properties.remove("departments");
                properties.remove("faculty");

                node.set("properties", properties);

                return objectMapper.writeValueAsString(node);
            }
            return objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            return String.format("{'error': 'Could not serialize entity of type %s'}",
                    entity.getClass().getSimpleName());
        }
    }
}
