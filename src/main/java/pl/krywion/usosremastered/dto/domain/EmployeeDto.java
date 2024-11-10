package pl.krywion.usosremastered.dto.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String pesel;
    private Long departmentId;
    private String departmentName;
    private List<Long> courseIds;
    private LocalDate hireDate;
}
