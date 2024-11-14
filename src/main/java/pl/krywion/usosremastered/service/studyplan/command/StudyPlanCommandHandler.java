package pl.krywion.usosremastered.service.studyplan.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudyPlanMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.repository.StudyPlanRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyPlanCommandHandler {

    private final StudyPlanRepository studyPlanRepository;
    private final StudyPlanMapper mapper;


    public ServiceResponse<StudyPlanDto> handle(CreateStudyPlanCommand createStudyPlanCommand) {
        StudyPlan studyPlan = mapper.toEntity(createStudyPlanCommand.studyPlanDto());
        studyPlanRepository.save(studyPlan);
        return ServiceResponse.success(
                mapper.toDto(studyPlan),
                "Study plan created successfully",
                HttpStatus.CREATED
        );
    }
}
