package pl.krywion.usosremastered.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.dto.auth.LoginUserDto;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SendEmailService sendEmailService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            SendEmailService sendEmailService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.sendEmailService = sendEmailService;
    }

    public User signUp(RegisterUserDto input) {
        User user = new User();
        user.setEmail(input.getEmail());

        if(input.getPassword() == null || input.getPassword().isEmpty()) {
            input.setPassword(generatePassword());
        }

        System.out.println("Generated password: " + input.getPassword());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(input.getRole());

        // Send email with password
        this.sendEmailService.sendEmail(user.getEmail(), "Account created", newStudentMessage(user.getEmail(), input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        System.out.println(input.getEmail());
        System.out.println(input.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    private String generatePassword() {
        int length = 12;
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-+";
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        List<Character> passwordChars = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(passwordChars);

        return passwordChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private String newStudentMessage(String email, String password) {
        String message = "Hello, Welcome on our University\n";
        message += "Your account has been created\n";
        message += "Email: " + email + "\n";
        message += "Password: " + password + "\n";
        message += "You can now log in to the system.";
        return message;
    }

}
