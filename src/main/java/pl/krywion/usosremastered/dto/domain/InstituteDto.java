package pl.krywion.usosremastered.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstituteDto {
    private Long id;
    private String name;
    private Long facultyId;
    private Long managerId;
    private String managerName;
}
