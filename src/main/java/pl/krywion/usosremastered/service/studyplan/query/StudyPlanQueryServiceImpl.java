package pl.krywion.usosremastered.service.studyplan.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudyPlanMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudyPlanRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StudyPlanQueryServiceImpl implements StudyPlanQueryService {
    private final StudyPlanRepository studyPlanRepository;
    private final StudyPlanMapper mapper;

    @Override
    public ServiceResponse<List<StudyPlanDto>> findAll() {
        List<StudyPlan> studyPlans = studyPlanRepository.findAll();
        return ServiceResponse.success(
                mapper.toDtoList(studyPlans),
                "Study plans found successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ServiceResponse<StudyPlanDto> findById(Long id) {
        StudyPlan studyPlan = studyPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Study plan with id %d not found", id)));
        return ServiceResponse.success(
                mapper.toDto(studyPlan),
                "Study plan found successfully",
                HttpStatus.OK
        );
    }
}
