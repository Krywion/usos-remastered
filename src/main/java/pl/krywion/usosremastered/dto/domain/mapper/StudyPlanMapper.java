package pl.krywion.usosremastered.dto.domain.mapper;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.entity.StudyPlan;

import java.util.List;

@Component
public class StudyPlanMapper {

    public StudyPlanDto toDto(StudyPlan entity) {
        StudyPlanDto dto = new StudyPlanDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public StudyPlan toEntity(StudyPlanDto dto) {
        StudyPlan entity = new StudyPlan();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }

    public List<StudyPlanDto> toDtoList(List<StudyPlan> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }
}
