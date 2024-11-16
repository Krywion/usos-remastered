package pl.krywion.usosremastered.service;

public interface NotificationService {
    void sendWelcomeMessage(String email, String password);
    void sendAccountUpdateNotification(String email, String message);
    void sendAccountDeleteNotification(String email);
    void sendPasswordResetMessage(String email, String password);
}
