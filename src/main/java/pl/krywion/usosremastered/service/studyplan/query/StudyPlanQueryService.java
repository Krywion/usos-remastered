package pl.krywion.usosremastered.service.studyplan.query;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

@Service
public interface StudyPlanQueryService {

    ServiceResponse<List<StudyPlanDto>> findAll();
    ServiceResponse<StudyPlanDto> findById(Long id);
}
