package pl.krywion.usosremastered.service.studyplan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.service.StudyPlanService;
import pl.krywion.usosremastered.service.studyplan.command.CreateStudyPlanCommand;
import pl.krywion.usosremastered.service.studyplan.command.StudyPlanCommandHandler;
import pl.krywion.usosremastered.service.studyplan.query.StudyPlanQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPlanServiceImpl implements StudyPlanService {

    private final StudyPlanCommandHandler studyPlanCommandHandler;
    private final StudyPlanQueryService studyPlanQueryService;

    @Override
    public StudyPlan createStudyPlan(StudyPlanDto studyPlanDto) {
        return studyPlanCommandHandler.handle(new CreateStudyPlanCommand(studyPlanDto));
    }

    @Override
    public StudyPlan getStudyPlan(Long id) {
        return studyPlanQueryService.findById(id);
    }

    @Override
    public List<StudyPlan> allStudyPlans() {
        return studyPlanQueryService.findAll();
    }
}
