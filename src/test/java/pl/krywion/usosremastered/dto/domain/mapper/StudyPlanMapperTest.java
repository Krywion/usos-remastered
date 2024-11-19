package pl.krywion.usosremastered.dto.domain.mapper;

import org.junit.jupiter.api.Test;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.entity.StudyPlan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudyPlanMapperTest {
    private final StudyPlanMapper mapper = new StudyPlanMapper();

    @Test
    void testToDto() {
        StudyPlan entity = createStudyPlan(1L, "Test Plan");
        StudyPlanDto dto = mapper.toDto(entity);
        assertStudyPlanDto(dto, entity);
    }

    @Test
    void testToEntity() {
        StudyPlanDto dto = createStudyPlanDto();
        StudyPlan entity = mapper.toEntity(dto);
        assertStudyPlanEntity(entity, dto);
    }

    @Test
    void testToDtoList() {
        List<StudyPlan> entities = List.of(
                createStudyPlan(1L, "Plan 1"),
                createStudyPlan(2L, "Plan 2")
        );
        List<StudyPlanDto> dtos = mapper.toDtoList(entities);
        assertEquals(2, dtos.size());
        assertStudyPlanDto(dtos.get(0), entities.get(0));
        assertStudyPlanDto(dtos.get(1), entities.get(1));
    }

    private StudyPlan createStudyPlan(Long id, String name) {
        StudyPlan plan = new StudyPlan();
        plan.setId(id);
        plan.setName(name);
        return plan;
    }

    private StudyPlanDto createStudyPlanDto() {
        StudyPlanDto dto = new StudyPlanDto();
        dto.setId(1L);
        dto.setName("Test Plan");
        return dto;
    }

    private void assertStudyPlanDto(StudyPlanDto dto, StudyPlan entity) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    private void assertStudyPlanEntity(StudyPlan entity, StudyPlanDto dto) {
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}