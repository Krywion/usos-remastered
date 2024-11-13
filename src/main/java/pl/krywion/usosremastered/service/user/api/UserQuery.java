package pl.krywion.usosremastered.service.user.api;

import pl.krywion.usosremastered.entity.User;

import java.util.List;


public interface UserQuery {
    List<User> getAllUsers();
    User findByEmail(String email);
}
