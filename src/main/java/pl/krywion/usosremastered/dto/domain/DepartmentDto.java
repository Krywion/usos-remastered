package pl.krywion.usosremastered.dto.domain;

import lombok.Data;


@Data
public class DepartmentDto {
    private Long id;
    private String name;
    private Long instituteId;
}
