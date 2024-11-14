package pl.krywion.usosremastered.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.service.studyplan.StudyPlanServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/study-plan")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class StudyPlanController {

    private final StudyPlanServiceImpl studyPlanService;

    public StudyPlanController(StudyPlanServiceImpl studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyPlanDto> getStudyPlan(@PathVariable Long id) {
        ServiceResponse<StudyPlanDto> response = studyPlanService.getStudyPlan(id);
        return ResponseEntity.status(response.getHttpStatus()).body(response.getData());
    }

    @GetMapping
    public ResponseEntity<List<StudyPlanDto>> allStudyPlans() {
        ServiceResponse<List<StudyPlanDto>> response = studyPlanService.allStudyPlans();
        return ResponseEntity.status(response.getHttpStatus()).body(response.getData());
    }
}
