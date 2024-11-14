package pl.krywion.usosremastered.service.studyplan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
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
    public ServiceResponse<StudyPlanDto> createStudyPlan(StudyPlanDto studyPlanDto) {
        return studyPlanCommandHandler.handle(new CreateStudyPlanCommand(studyPlanDto));
    }

    @Override
    public ServiceResponse<StudyPlanDto> getStudyPlan(Long id) {
        return studyPlanQueryService.findById(id);
    }

    @Override
    public ServiceResponse<List<StudyPlanDto>> allStudyPlans() {
        return studyPlanQueryService.findAll();
    }
}
