package pl.krywion.usosremastered.dto.domain.mapper;

import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.entity.Student;
import pl.krywion.usosremastered.entity.StudyPlan;

import java.util.Collections;
import java.util.List;

@Component
public class StudentMapper {

    public StudentDto toDto(Student entity) {
        StudentDto dto = new StudentDto();
        dto.setAlbumNumber(entity.getAlbumNumber());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());

        if(entity.getStudyPlans() != null) {
            dto.setStudyPlanIds(entity.getStudyPlans().stream().map(StudyPlan::getId).toList());
        }

        if(entity.getMasterThesis() != null) {
            dto.setMasterThesisIds(Collections.singletonList(entity.getMasterThesis().getId()));
        }

        return dto;
    }

    public List<StudentDto> toDtoList(List<Student> entityLists) {
        if(entityLists == null) {
            return Collections.emptyList();
        }

        return entityLists.stream()
                .map(this::toDto)
                .toList();
    }

    public Student toEntity(StudentDto dto) {
        Student entity = new Student();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());

        return entity;
    }
}
