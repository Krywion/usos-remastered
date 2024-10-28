package pl.krywion.usosremastered.service.impl;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.UserService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

}