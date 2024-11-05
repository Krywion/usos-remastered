package pl.krywion.usosremastered.dto.response;

import lombok.Data;
import pl.krywion.usosremastered.dto.EmployeeDto;

@Data
public class EmployeeResponseDto {
    private EmployeeDto employeeDto;
    private String message;
    private boolean success;
}
