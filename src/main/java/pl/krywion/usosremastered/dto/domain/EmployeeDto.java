package pl.krywion.usosremastered.dto.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDto {
    @Schema(description = "Employee's PESEL number", example = "54042132492")
    private String pesel;

    @Schema(description = "Employee's first name", example = "Jan")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Kowalski")
    private String lastName;

    @Schema(description = "Employee's email address", example = "jan.kowalski@edu.pl")
    private String email;

    @Schema(description = "ID of employee's department", example = "1")
    private Long departmentId;

    @Schema(description = "Name of employee's department", example = "Department of Software Engineering")
    private String departmentName;

    @Schema(description = "List of employee's course IDs")
    private List<Long> courseIds;

    @Schema(description = "Employee's hire date", example = "2021-01-01")
    private LocalDate hireDate;
}
