package pl.krywion.usosremastered.service.studyplan.query;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.entity.StudyPlan;

import java.util.List;

@Service
public interface StudyPlanQueryService {

    List<StudyPlan> findAll();
    StudyPlan findById(Long id);
}
