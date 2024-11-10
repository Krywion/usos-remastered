package pl.krywion.usosremastered.service;


import pl.krywion.usosremastered.dto.domain.DepartmentDto;
import pl.krywion.usosremastered.dto.domain.FacultyDto;
import pl.krywion.usosremastered.dto.domain.InstituteDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;

import java.util.List;

public interface OrganizationalStructureService {
    ServiceResponse<List<FacultyDto>> getAllFaculties();

    ServiceResponse<List<InstituteDto>> getInstitutesForFaculty(Long facultyId);

    ServiceResponse<DepartmentDto> getDepartmentsForInstitute(Long instituteId);

    ServiceResponse<FacultyDto> createFaculty(FacultyDto facultyDto);

    ServiceResponse<InstituteDto> createInstitute(InstituteDto instituteDto);

    ServiceResponse<DepartmentDto> createDepartment(DepartmentDto departmentDto);
}
