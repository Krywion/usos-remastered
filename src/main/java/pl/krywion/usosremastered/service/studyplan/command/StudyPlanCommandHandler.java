package pl.krywion.usosremastered.service.studyplan.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.mapper.StudyPlanMapper;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.repository.StudyPlanRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyPlanCommandHandler {

    private final StudyPlanRepository studyPlanRepository;
    private final StudyPlanMapper mapper;


    public StudyPlan handle(CreateStudyPlanCommand command) {
        StudyPlan studyPlan = mapper.toEntity(command.studyPlanDto());
        studyPlanRepository.save(studyPlan);
        return studyPlan;
    }
}
