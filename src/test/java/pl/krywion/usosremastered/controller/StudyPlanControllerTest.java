package pl.krywion.usosremastered.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.dto.domain.mapper.StudyPlanMapper;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.StudyPlan;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.service.studyplan.StudyPlanServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("StudyPlan Controller tests")
@ExtendWith(MockitoExtension.class)
class StudyPlanControllerTest {

    @Mock
    private StudyPlanServiceImpl studyPlanService;

    @Mock
    private StudyPlanMapper mapper;

    @InjectMocks
    private StudyPlanController studyPlanController;

    private StudyPlan studyPlan;
    private StudyPlanDto studyPlanDto;

    @BeforeEach
    void setUp() {
        studyPlan = createStudyPlan();
        studyPlanDto = createStudyPlanDto();
    }

    @DisplayName("Should create study plan")
    @Test
    void shouldGetStudyPlan() {
        // given
        Long studyPlanId = 1L;
        when(studyPlanService.getStudyPlan(studyPlanId)).thenReturn(studyPlan);
        when(mapper.toDto(studyPlan)).thenReturn(studyPlanDto);

        // when
        ResponseEntity<ServiceResponse<StudyPlanDto>> response = studyPlanController.getStudyPlan(studyPlanId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(studyPlanDto);
        assertThat(response.getBody().getMessage()).isEqualTo("Study plan retrieved successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
        verify(studyPlanService).getStudyPlan(studyPlanId);
    }

    @DisplayName("Should get all study plans")
    @Test
    void shouldGetAllStudyPlans() {
        // given
        List<StudyPlan> studyPlans = Arrays.asList(studyPlan, studyPlan);
        List<StudyPlanDto> studyPlanDtos = Arrays.asList(studyPlanDto, studyPlanDto);

        when(studyPlanService.allStudyPlans()).thenReturn(studyPlans);
        when(mapper.toDtoList(studyPlans)).thenReturn(studyPlanDtos);

        // when
        ResponseEntity<ServiceResponse<List<StudyPlanDto>>> response = studyPlanController.allStudyPlans();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2);
        assertThat(response.getBody().getMessage()).isEqualTo("All study plans retrieved successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
    }

    @DisplayName("Should handle ResourceNotFoundException")
    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        Long nonExistentId = 999L;
        when(studyPlanService.getStudyPlan(nonExistentId))
                .thenThrow(new ResourceNotFoundException("Study plan not found"));

        // when/then
        assertThatThrownBy(() -> studyPlanController.getStudyPlan(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Study plan not found");
    }

    @DisplayName("Should return empty list when no study plans")
    @Test
    void shouldReturnEmptyListWhenNoStudyPlans() {
        // given
        when(studyPlanService.allStudyPlans()).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());

        // when
        ResponseEntity<ServiceResponse<List<StudyPlanDto>>> response = studyPlanController.allStudyPlans();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEmpty();
        assertThat(response.getBody().getMessage()).isEqualTo("All study plans retrieved successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
    }

    private StudyPlan createStudyPlan() {
        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setId(1L);
        studyPlan.setName("Computer Science");
        return studyPlan;
    }

    private StudyPlanDto createStudyPlanDto() {
        StudyPlanDto dto = new StudyPlanDto();
        dto.setId(1L);
        dto.setName("Computer Science");
        return dto;
    }
}
