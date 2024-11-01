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
    private Long albumNumber;
    private String firstName;
    private String lastName;
    private String email;
    private Long studyPlanId;
    private List<Long> masterThesisIds;


}
