package pl.krywion.usosremastered.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.domain.DepartmentDto;
import pl.krywion.usosremastered.dto.domain.FacultyDto;
import pl.krywion.usosremastered.dto.domain.InstituteDto;
import pl.krywion.usosremastered.dto.response.ApiResponse;
import pl.krywion.usosremastered.entity.Dean;
import pl.krywion.usosremastered.entity.Department;
import pl.krywion.usosremastered.entity.Faculty;
import pl.krywion.usosremastered.entity.Institute;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.exception.base.BaseException;
import pl.krywion.usosremastered.repository.DepartmentRepository;
import pl.krywion.usosremastered.repository.EmployeeRepository;
import pl.krywion.usosremastered.repository.FacultyRepository;
import pl.krywion.usosremastered.repository.InstituteRepository;
import pl.krywion.usosremastered.service.OrganizationalStructureService;

import java.util.List;

@Service
public class OrganizationalStructureImpl implements OrganizationalStructureService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationalStructureImpl.class);
    private final FacultyRepository facultyRepository;
    private final InstituteRepository instituteRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public OrganizationalStructureImpl(
            FacultyRepository facultyRepository,
            InstituteRepository instituteRepository,
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper) {
        this.facultyRepository = facultyRepository;
        this.instituteRepository = instituteRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<List<FacultyDto>> getAllFaculties() {
        try {
            List<Faculty> faculties = facultyRepository.findAll();
            List<FacultyDto> facultyDtos = faculties.stream()
                    .map(faculty -> {
                        FacultyDto facultyDto = modelMapper.map(faculty, FacultyDto.class);
                        facultyDto.setDeanName(faculty.getDean().getFirstName() + " " + faculty.getDean().getLastName());
                        return facultyDto;
                    })
                    .toList();

            return ApiResponse.success(
                    facultyDtos,
                    "Faculties retrieved successfully",
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("Error retrieving faculties", e);
            throw new ValidationException("Error fetching faculties: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<InstituteDto>> getInstitutesForFaculty(Long facultyId) {
        return null;
    }

    @Override
    public ApiResponse<DepartmentDto> getDepartmentsForInstitute(Long instituteId) {
        return null;
    }

    public ApiResponse<FacultyDto> createFaculty(FacultyDto facultyDto) {
        try {
            Faculty faculty = new Faculty();
            faculty.setName(facultyDto.getName());
            faculty.setPostalCode(facultyDto.getPostalCode());
            faculty.setEstablishmentYear(facultyDto.getEstablishmentYear());

            if(facultyDto.getDeanId() != null) {
                Dean dean = (Dean) employeeRepository.findById(facultyDto.getDeanId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Dean with id " + facultyDto.getDeanId() + " not found"));

                faculty.setDean(dean);
            }

            Faculty savedFaculty = facultyRepository.save(faculty);

            FacultyDto savedDto = modelMapper.map(savedFaculty, FacultyDto.class);

            if (savedFaculty.getDean() != null) {
                savedDto.setDeanName(savedFaculty.getDean().getFirstName() + " " + savedFaculty.getDean().getLastName());
            }

            return ApiResponse.success(
                    savedDto,
                    "Faculty created successfully",
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            log.error("Error creating faculty", e);
            throw e;
        }
    }

    public ApiResponse<InstituteDto> createInstitute(InstituteDto instituteDto) {
        try {
            Faculty faculty = facultyRepository.findById(instituteDto.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Faculty with id " + instituteDto.getFacultyId() + " not found"));

            Institute institute = new Institute();
            institute.setName(instituteDto.getName());
            institute.setFaculty(faculty);

            Institute savedInstitute = instituteRepository.save(institute);

            InstituteDto savedDto = modelMapper.map(savedInstitute, InstituteDto.class);
            savedDto.setFacultyId(faculty.getId());

            return ApiResponse.success(
                    savedDto,
                    "Institute created successfully",
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            if ( e instanceof BaseException) {
                throw e;
            } else {
                log.error("Error creating institute", e);
                throw new RuntimeException("Error creating institute: " + e.getMessage());
            }
        }
    }

    public ApiResponse<DepartmentDto> createDepartment(DepartmentDto departmentDto) {
        try {
            Institute institute = instituteRepository.findById(departmentDto.getInstituteId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Institute with id " + departmentDto.getInstituteId() + " not found"));

            Department department = new Department();
            department.setName(departmentDto.getName());
            department.setInstitute(institute);

            Department savedDepartment = departmentRepository.save(department);

            DepartmentDto savedDto = modelMapper.map(savedDepartment, DepartmentDto.class);
            savedDto.setInstituteId(institute.getId());

            return ApiResponse.success(
                    savedDto,
                    "Department created successfully",
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            if ( e instanceof BaseException) {
                throw e;
            } else {
                log.error("Error creating department", e);
                throw new RuntimeException("Error creating department: " + e.getMessage());
            }
        }
    }
}
