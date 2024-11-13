package pl.krywion.usosremastered.service.user.api;

import pl.krywion.usosremastered.entity.User;

public interface UserCreator {
    User createUserForStudent(String email);
    User createUserForEmployee(String email);
}
