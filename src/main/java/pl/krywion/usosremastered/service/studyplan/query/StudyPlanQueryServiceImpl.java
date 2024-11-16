package pl.krywion.usosremastered.service.studyplan.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudyPlanRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StudyPlanQueryServiceImpl implements StudyPlanQueryService {
    private final StudyPlanRepository studyPlanRepository;

    @Override
    public List<StudyPlan> findAll() {
        return studyPlanRepository.findAll();
    }

    @Override
    public StudyPlan findById(Long id) {
        return studyPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Study plan with id %d not found", id)));
    }
}
