package pl.krywion.usosremastered.service;

public interface PasswordManagementService {
    String generateSecurePassword();
    String encodePassword(String rawPassword);
}
