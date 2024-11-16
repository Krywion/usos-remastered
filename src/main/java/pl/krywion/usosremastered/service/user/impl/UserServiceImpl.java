package pl.krywion.usosremastered.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.service.UserService;
import pl.krywion.usosremastered.service.user.api.UserCreator;
import pl.krywion.usosremastered.service.user.api.UserManager;
import pl.krywion.usosremastered.service.user.api.UserQuery;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserQuery userQuery;
    private final UserCreator userCreator;
    private final UserManager userManager;

    @Override
    public User createUserForStudent(String email) {
        return userCreator.createUserForStudent(email);
    }

    @Override
    public User createUserForEmployee(String email) {
        return userCreator.createUserForEmployee(email);
    }

    @Override
    public void deleteUser(String email) {
        userManager.deleteUser(email);
    }

    @Override
    public User updateUserEmail(String oldEmail, String newEmail) {
        return userManager.updateUserEmail(oldEmail, newEmail);
    }

    @Override
    public User findByEmail(String email) {
        return userQuery.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userQuery.getAllUsers();
    }

    @Override
    public User resetPassword(String email) {
        return userManager.resetPassword(email);
    }
}