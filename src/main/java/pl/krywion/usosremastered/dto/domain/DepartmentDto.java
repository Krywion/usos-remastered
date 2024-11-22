package pl.krywion.usosremastered.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepartmentDto {
    private Long id;
    private String name;
    private Long instituteId;
}
