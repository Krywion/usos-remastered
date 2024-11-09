package pl.krywion.usosremastered.dto.domain;

import lombok.Data;

@Data
public class FacultyDto {
    private Long id;
    private String name;
    private String postalCode;
    private Integer establishmentYear;
    private Long deanId;
    private String deanName;
}
