package pl.krywion.usosremastered.dto.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.krywion.usosremastered.audit.Identifiable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentDto implements Identifiable {

    private Long albumNumber;
    private String firstName;
    private String lastName;
    private String email;
    private Long studyPlanId;
    private List<Long> masterThesisIds;


    @Override
    public Long getIdentifier() {
        return albumNumber;
    }
}
