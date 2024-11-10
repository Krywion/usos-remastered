package pl.krywion.usosremastered.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.service.impl.StudyPlanServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/student-plan")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class StudyPlanController {

    private final StudyPlanServiceImpl studyPlanService;

    public StudyPlanController(StudyPlanServiceImpl studyPlanService) {
        this.studyPlanService = studyPlanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyPlanDto> getStudyPlan(@PathVariable Long id) {
        StudyPlanDto studyPlan = studyPlanService.getStudyPlan(id);
        return ResponseEntity.ok(studyPlan);
    }

    @GetMapping
    public ResponseEntity<List<StudyPlanDto>> allStudyPlans() {
        List<StudyPlanDto> studyPlans = studyPlanService.allStudyPlans();
        System.out.println(studyPlans);
        return ResponseEntity.ok(studyPlans);
    }
}
