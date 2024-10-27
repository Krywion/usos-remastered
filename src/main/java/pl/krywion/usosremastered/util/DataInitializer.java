package pl.krywion.usosremastered.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.krywion.usosremastered.dto.RegisterUserDto;
import pl.krywion.usosremastered.service.AuthenticationService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${dev.email}")
    private String devEmail;
    @Value("${dev.password}")
    private String devPassword;

    private final AuthenticationService authenticationService;

    public DataInitializer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void run(String... args) {
        RegisterUserDto user = new RegisterUserDto();
        user.setEmail(devEmail);
        user.setPassword(devPassword);
        authenticationService.signUp(user);
    }
}
