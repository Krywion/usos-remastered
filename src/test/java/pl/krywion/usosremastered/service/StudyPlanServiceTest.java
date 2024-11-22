package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudyPlanMapper;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.StudyPlanRepository;
import pl.krywion.usosremastered.service.studyplan.StudyPlanServiceImpl;
import pl.krywion.usosremastered.service.studyplan.command.StudyPlanCommandHandler;
import pl.krywion.usosremastered.service.studyplan.query.StudyPlanQueryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyPlan service tests")
class StudyPlanServiceTest {

    @Mock
    private StudyPlanRepository studyPlanRepository;
    @Mock
    private StudyPlanMapper studyPlanMapper;

    private StudyPlanServiceImpl studyPlanService;

    @BeforeEach
    void setUp() {
        StudyPlanCommandHandler commandHandler = new StudyPlanCommandHandler(studyPlanRepository, studyPlanMapper);
        StudyPlanQueryServiceImpl queryService = new StudyPlanQueryServiceImpl(studyPlanRepository);
        studyPlanService = new StudyPlanServiceImpl(commandHandler, queryService);
    }

    @Test
    @DisplayName("Should create study plan")
    void shouldCreateStudyPlan() {
        // given
        StudyPlanDto dto = new StudyPlanDto();
        dto.setName("Computer Science");

        StudyPlan entity = new StudyPlan();
        entity.setName("Computer Science");
        entity.setId(1L);

        when(studyPlanMapper.toEntity(dto)).thenReturn(entity);
        when(studyPlanRepository.save(any(StudyPlan.class))).thenReturn(entity);

        // when
        StudyPlan result = studyPlanService.createStudyPlan(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Computer Science");
        verify(studyPlanRepository).save(entity);
    }

    @Test
    @DisplayName("Should get study plan")
    void shouldGetStudyPlan() {
        // given
        Long id = 1L;
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(id);
        studyPlan.setName("Computer Science");

        when(studyPlanRepository.findById(id)).thenReturn(Optional.of(studyPlan));

        // when
        StudyPlan result = studyPlanService.getStudyPlan(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should throw exception when study plan not found")
    void shouldThrowExceptionWhenStudyPlanNotFound() {
        // given
        Long id = 1L;
        when(studyPlanRepository.findById(id)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> studyPlanService.getStudyPlan(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Study plan with id 1 not found");
    }

    @Test
    @DisplayName("Should get all study plans")
    void shouldGetAllStudyPlans() {
        // given
        StudyPlan plan1 = new StudyPlan();
        plan1.setId(1L);
        plan1.setName("Computer Science");

        StudyPlan plan2 = new StudyPlan();
        plan2.setId(2L);
        plan2.setName("Mathematics");

        List<StudyPlan> studyPlans = Arrays.asList(plan1, plan2);
        when(studyPlanRepository.findAll()).thenReturn(studyPlans);

        // when
        List<StudyPlan> result = studyPlanService.allStudyPlans();

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(StudyPlan::getName)
                .containsExactly("Computer Science", "Mathematics");
    }

    @Test
    @DisplayName("Should return empty list when no study plans")
    void shouldReturnEmptyListWhenNoStudyPlans() {
        // given
        when(studyPlanRepository.findAll()).thenReturn(List.of());

        // when
        List<StudyPlan> result = studyPlanService.allStudyPlans();

        // then
        assertThat(result).isEmpty();
    }
}