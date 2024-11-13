package pl.krywion.usosremastered.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.user.api.UserQuery;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserQueryImpl implements UserQuery {

    private final UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
