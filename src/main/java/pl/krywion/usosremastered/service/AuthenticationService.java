package pl.krywion.usosremastered.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.dto.auth.LoginUserDto;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordManagementService passwordManagementService;
    private final AuthenticationManager authenticationManager;
    private final NotificationService notificationService;

    @Transactional
    public User signUp(RegisterUserDto input) {
        String password = input.getPassword();
        if(input.getPassword() == null || input.getPassword().isEmpty()) {
            password = passwordManagementService.generateSecurePassword();
        }

        User user = createUserFromDto(input, password);
        User savedUser = userRepository.save(user);

        notificationService.sendWelcomeMessage(savedUser.getEmail(), password);

        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
        );

        authenticationManager.authenticate(authentication);
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User createUserFromDto(RegisterUserDto input, String password) {
        User user = new User();
        user.setEmail(input.getEmail());
        user.setRole(input.getRole());
        user.setPassword(passwordManagementService.encodePassword(password));
        return user;
    }

}
