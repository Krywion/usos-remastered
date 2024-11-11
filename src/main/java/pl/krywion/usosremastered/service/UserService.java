package pl.krywion.usosremastered.service;

import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.entity.User;

import java.util.List;

public interface UserService {

    User createUser(String email, Role role);

    List<User> allUsers();

    User updateUser(String email, Role role);

    User deleteUser(String email);
}
