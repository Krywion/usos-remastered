package pl.krywion.usosremastered.service;

import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.entity.User;

import java.util.List;

@Service
public interface UserService {
    User createUserForStudent(String email);
    User createUserForEmployee(String email);
    void deleteUser(String email);
    User updateUserEmail(String oldEmail, String newEmail);
    User findByEmail(String email);
    List<User> getAllUsers();
    User resetPassword(String email);
}
