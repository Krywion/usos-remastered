package pl.krywion.usosremastered.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String pesel;
    private Long departmentId;
    private List<Long> courseIds;
    private List<Long> masterThesisIds;

}
