package pl.krywion.usosremastered.service.user.api;


import pl.krywion.usosremastered.entity.User;

public interface UserManager {
    void deleteUser(String email);
    User updateUserEmail(String oldEmail, String newEmail);
    User resetPassword(String email);
}
