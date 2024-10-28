package pl.krywion.usosremastered.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentDto {
    private String firstName;
    private String lastName;
    private String email;
    private String albumNumber;
    private Long studyPlanId;
    private List<Long> masterThesisIds;
}
