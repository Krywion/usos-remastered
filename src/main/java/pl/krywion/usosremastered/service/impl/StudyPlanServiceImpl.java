package pl.krywion.usosremastered.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.StudyPlanDto;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.service.StudyPlanService;

import java.util.List;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    private final StudyPlanRepository studyPlanRepository;
    private final ModelMapper modelMapper;

    public StudyPlanServiceImpl(
            StudyPlanRepository studyPlanRepository,
            ModelMapper modelMapper
    ) {
        this.studyPlanRepository = studyPlanRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudyPlanDto createStudyPlan(StudyPlanDto studyPlanDto) {
        StudyPlan studyPlan;

        studyPlan = modelMapper.map(studyPlanDto, StudyPlan.class);
        studyPlanRepository.save(studyPlan);

        return modelMapper.map(studyPlan, StudyPlanDto.class);
    }

    @Override
    public StudyPlanDto getStudyPlan(Long id) {
        StudyPlan studyPlan = studyPlanRepository.findById(id).orElse(null);

        return modelMapper.map(studyPlan, StudyPlanDto.class);
    }

    @Override
    public List<StudyPlanDto> allStudyPlans() {
        System.out.println("StudyPlanServiceImpl.allStudyPlans");
        return studyPlanRepository.findAll().stream()
                .map(studyPlan -> modelMapper.map(studyPlan, StudyPlanDto.class))
                .collect(java.util.stream.Collectors.toList());
    }
}
