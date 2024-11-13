package pl.krywion.usosremastered.service.student.query;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.StudentDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

@Service
public interface StudentQueryService {
    ServiceResponse<List<StudentDto>> findAll();
    ServiceResponse<StudentDto> findByAlbumNumber(Long albumNumber);
    ServiceResponse<List<StudentDto>> findByLastName(String lastName);
    ServiceResponse<List<StudentDto>> findByFirstName(String firstName);
    ServiceResponse<StudentDto> findByEmail(String email);
}
