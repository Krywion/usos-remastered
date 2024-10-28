package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.StudyPlanDto;

import java.util.List;

public interface StudyPlanService {
    StudyPlanDto createStudyPlan(StudyPlanDto studyPlanDto);

    StudyPlanDto getStudyPlan(Long id);

    List<StudyPlanDto> allStudyPlans();
}
