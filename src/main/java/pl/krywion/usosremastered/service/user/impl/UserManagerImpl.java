package pl.krywion.usosremastered.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.user.api.UserManager;

@RequiredArgsConstructor
@Transactional
@Service
public class UserManagerImpl implements UserManager {
    private final UserRepository userRepository;

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
    }

    @Override
    public User updateUserEmail(String oldEmail, String newEmail) {
        User user = userRepository.findByEmail(oldEmail).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + oldEmail));
        if(userRepository.existsByEmail(newEmail)) {
            throw new ValidationException("User with email: " + newEmail + " already exists");
        }
        user.setEmail(newEmail);

        return userRepository.save(user);
    }
}
