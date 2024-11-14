package pl.krywion.usosremastered.dto.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Student Data Transfer Object")
public class StudentDto implements Identifiable {

    @Schema(description = "Student's album number - unique identifier", example = "123456")
    private Long albumNumber;

    @Schema(description = "Student's first name", example = "Jan")
    private String firstName;

    @Schema(description = "Student's last name", example = "Kowalski")
    private String lastName;

    @Schema(description = "Student's email address", example = "jan.kowalski@student.edu.pl")
    private String email;

    @Schema(description = "ID of student's study plan", example = "1")
    private List<Long> studyPlanIds;

    @Schema(description = "List of student's master thesis IDs")
    private List<Long> masterThesisIds;


    @Override
    public String getIdentifier() {
        return albumNumber.toString();
    }
}
