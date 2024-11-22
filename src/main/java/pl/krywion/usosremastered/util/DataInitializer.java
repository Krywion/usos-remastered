package pl.krywion.usosremastered.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.dto.domain.DepartmentDto;
import pl.krywion.usosremastered.dto.domain.FacultyDto;
import pl.krywion.usosremastered.dto.domain.InstituteDto;
import pl.krywion.usosremastered.dto.domain.StudyPlanDto;
import pl.krywion.usosremastered.service.common.AuthenticationService;
import pl.krywion.usosremastered.service.OrganizationalStructureService;
import pl.krywion.usosremastered.service.StudyPlanService;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${dev.email}")
    private String devEmail;
    @Value("${dev.password}")
    private String devPassword;

    private final AuthenticationService authenticationService;
    private final StudyPlanService studyPlanService;
    private final OrganizationalStructureService organizationalStructureService;

    @Override
    public void run(String... args) {
        addDefaultUser();
        addStudyPlans();
        addOrganizationalUnits();
    }

    private void addOrganizationalUnits() {
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setName("Faculty of Mathematics and Computer Science");
        facultyDto.setPostalCode("00-000");
        facultyDto.setEstablishmentYear(1990);
        facultyDto.setDeanId(null);
        facultyDto.setDeanName(null);

        organizationalStructureService.createFaculty(facultyDto);

        InstituteDto instituteDto = new InstituteDto();
        instituteDto.setName("Institute of Mathematics");
        instituteDto.setFacultyId(1L);
        instituteDto.setManagerId(null);
        instituteDto.setManagerName(null);

        organizationalStructureService.createInstitute(instituteDto);

        instituteDto = new InstituteDto();
        instituteDto.setName("Institute of Computer Science");
        instituteDto.setFacultyId(1L);
        instituteDto.setManagerId(null);
        instituteDto.setManagerName(null);

        organizationalStructureService.createInstitute(instituteDto);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName("Department of Algebra");
        departmentDto.setInstituteId(1L);

        organizationalStructureService.createDepartment(departmentDto);

        departmentDto = new DepartmentDto();
        departmentDto.setName("Department of Geometry");
        departmentDto.setInstituteId(1L);

        organizationalStructureService.createDepartment(departmentDto);

        departmentDto = new DepartmentDto();
        departmentDto.setName("Department of Computer Science");
        departmentDto.setInstituteId(2L);

        organizationalStructureService.createDepartment(departmentDto);

        departmentDto = new DepartmentDto();
        departmentDto.setName("Department of Software Engineering");
        departmentDto.setInstituteId(2L);

        organizationalStructureService.createDepartment(departmentDto);
    }

    private void addDefaultUser() {
        RegisterUserDto user = new RegisterUserDto();
        user.setEmail(devEmail);
        user.setPassword(devPassword);
        user.setRole(Role.ADMIN);
        authenticationService.signUp(user);
    }

    private void addStudyPlans() {
        StudyPlanDto mathematics = new StudyPlanDto();
        mathematics.setName("Mathematics");
        studyPlanService.createStudyPlan(mathematics);

        StudyPlanDto physics = new StudyPlanDto();
        physics.setName("Physics");
        studyPlanService.createStudyPlan(physics);

        StudyPlanDto chemistry = new StudyPlanDto();
        chemistry.setName("Chemistry");
        studyPlanService.createStudyPlan(chemistry);

        StudyPlanDto biology = new StudyPlanDto();
        biology.setName("Biology");
        studyPlanService.createStudyPlan(biology);
    }
}
