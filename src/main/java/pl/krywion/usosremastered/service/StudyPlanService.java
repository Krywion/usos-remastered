package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.entity.StudyPlan;

import java.util.List;

public interface StudyPlanService {
    StudyPlan createStudyPlan(StudyPlanDto studyPlanDto);

    StudyPlan getStudyPlan(Long id);

    List<StudyPlan> allStudyPlans();
}
