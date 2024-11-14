package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface StudyPlanService {
    ServiceResponse<StudyPlanDto> createStudyPlan(StudyPlanDto studyPlanDto);

    ServiceResponse<StudyPlanDto> getStudyPlan(Long id);

    ServiceResponse<List<StudyPlanDto>> allStudyPlans();
}
