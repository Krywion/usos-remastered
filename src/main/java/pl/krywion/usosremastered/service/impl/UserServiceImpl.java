package pl.krywion.usosremastered.service.impl;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.AuthenticationService;
import pl.krywion.usosremastered.service.UserService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public User createUser(String email, Role role) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail(email);
        registerUserDto.setRole(role);
        return authenticationService.signUp(registerUserDto);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String email, Role role) {
        return null;
    }

    public User deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
        return user;
    }

}