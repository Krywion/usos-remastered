package pl.krywion.usosremastered.service;


import pl.krywion.usosremastered.dto.domain.DepartmentDto;
import pl.krywion.usosremastered.dto.domain.FacultyDto;
import pl.krywion.usosremastered.dto.domain.InstituteDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;

import java.util.List;

public interface OrganizationalStructureService {
    ApiResponse<List<FacultyDto>> getAllFaculties();

    ApiResponse<List<InstituteDto>> getInstitutesForFaculty(Long facultyId);

    ApiResponse<DepartmentDto> getDepartmentsForInstitute(Long instituteId);

    ApiResponse<FacultyDto> createFaculty(FacultyDto facultyDto);

    ApiResponse<InstituteDto> createInstitute(InstituteDto instituteDto);

    ApiResponse<DepartmentDto> createDepartment(DepartmentDto departmentDto);
}
