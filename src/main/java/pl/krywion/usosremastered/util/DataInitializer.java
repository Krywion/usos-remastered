package pl.krywion.usosremastered.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.dto.StudyPlanDto;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.StudyPlanService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${dev.email}")
    private String devEmail;
    @Value("${dev.password}")
    private String devPassword;

    private final AuthenticationService authenticationService;
    private final StudyPlanService studyPlanService;

    public DataInitializer(
            AuthenticationService authenticationService,
            StudyPlanService studyPlanService
    ) {
        this.authenticationService = authenticationService;
        this.studyPlanService = studyPlanService;
    }

    @Override
    public void run(String... args) {
        addDefaultUser();
        addStudyPlans();
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
