package pl.krywion.usosremastered.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.service.PasswordManagementService;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PasswordManagementServiceImpl implements PasswordManagementService {

    private final PasswordEncoder passwordEncoder;

    public PasswordManagementServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generateSecurePassword() {
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

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
