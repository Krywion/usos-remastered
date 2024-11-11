package pl.krywion.usosremastered.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.krywion.usosremastered.service.NotificationService;

@Service
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    @Override
    public void sendWelcomeMessage(String email, String password) {
        String message = buildWelcomeMessage(email, password);
        sendEmail(email, "Welcome to University System", message);
    }

    @Override
    public void sendAccountUpdateNotification(String email, String message) {
        sendEmail(email, "Account Updated", message);
    }

    @Override
    public void sendAccountDeleteNotification(String email) {
        sendEmail(email, "Account Deleted", "Your account in University System has been deleted");
    }

    private String buildWelcomeMessage(String email, String password) {
        return String.format("""
            Hello, Welcome to our University
            Your account has been created
            Email: %s
            Password: %s
            You can now log in to the system.""",
                email, password);
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
