package pl.krywion.usosremastered.service;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}
