package pl.krywion.usosremastered.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.krywion.usosremastered.dto.StudentDto;

@Data
@AllArgsConstructor
public class StudentResponseDto {
    private StudentDto studentDto;
    private String message;
    private boolean success;


}
