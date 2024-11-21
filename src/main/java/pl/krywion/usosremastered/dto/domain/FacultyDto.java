package pl.krywion.usosremastered.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FacultyDto {
    private Long id;
    private String name;
    private String postalCode;
    private Integer establishmentYear;
    private Long deanId;
    private String deanName;
}
