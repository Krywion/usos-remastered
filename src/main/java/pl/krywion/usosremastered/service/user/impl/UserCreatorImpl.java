package pl.krywion.usosremastered.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.NotificationService;
import pl.krywion.usosremastered.service.PasswordManagementService;
import pl.krywion.usosremastered.service.user.api.UserCreator;

@RequiredArgsConstructor
@Transactional
@Service
public class UserCreatorImpl implements UserCreator {

    private final UserRepository userRepository;
    private final PasswordManagementService passwordManagementService;
    private final NotificationService notificationService;

    @Override
    public User createUserForStudent(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new ValidationException("User with email: " + email + " already exists");
        }

        String password = passwordManagementService.generateSecurePassword();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordManagementService.encodePassword(password));
        user.setRole(Role.STUDENT);

        User savedUser = userRepository.save(user);
        notificationService.sendWelcomeMessage(savedUser.getEmail(), password);

        return savedUser;
    }

    @Override
    public User createUserForEmployee(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new ValidationException("User with email: " + email + " already exists");
        }

        String password = passwordManagementService.generateSecurePassword();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordManagementService.encodePassword(password));
        user.setRole(Role.EMPLOYEE);

        User savedUser = userRepository.save(user);
        notificationService.sendWelcomeMessage(savedUser.getEmail(), password);

        return savedUser;
    }
}
