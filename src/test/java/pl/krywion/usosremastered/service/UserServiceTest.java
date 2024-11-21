package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.exception.ValidationException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.user.impl.UserCreatorImpl;
import pl.krywion.usosremastered.service.user.impl.UserManagerImpl;
import pl.krywion.usosremastered.service.user.impl.UserQueryImpl;
import pl.krywion.usosremastered.service.user.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordManagementService passwordManagementService;
    @Mock
    private NotificationService notificationService;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                new UserQueryImpl(userRepository),
                new UserCreatorImpl(userRepository, passwordManagementService, notificationService),
                new UserManagerImpl(userRepository, passwordManagementService, notificationService)
        );
    }

    @Test
    @DisplayName("Should create user for student")
    void shouldCreateUserForStudent() {
        // given
        String email = "student@test.com";
        String password = "password123";
        User user = createUser(email, Role.STUDENT);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordManagementService.generateSecurePassword()).thenReturn(password);
        when(passwordManagementService.encodePassword(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.createUserForStudent(email);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getRole()).isEqualTo(Role.STUDENT);
        verify(notificationService).sendWelcomeMessage(email, password);
    }

    @Test
    @DisplayName("Should create user for employee")
    void shouldCreateUserForEmployee() {
        // given
        String email = "employee@test.com";
        String password = "password123";
        User user = createUser(email, Role.EMPLOYEE);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordManagementService.generateSecurePassword()).thenReturn(password);
        when(passwordManagementService.encodePassword(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.createUserForEmployee(email);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getRole()).isEqualTo(Role.EMPLOYEE);
        verify(notificationService).sendWelcomeMessage(email, password);
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing email")
    void shouldThrowExceptionWhenCreatingUserWithExistingEmail() {
        // given
        String email = "existing@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when/then
        assertThatThrownBy(() -> userService.createUserForStudent(email))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        // given
        String email = "test@test.com";
        User user = createUser(email, Role.STUDENT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        userService.deleteUser(email);

        // then
        verify(userRepository).delete(user);
        verify(notificationService).sendAccountDeleteNotification(email);
    }

    @Test
    @DisplayName("Should update user email")
    void shouldUpdateUserEmail() {
        // given
        String oldEmail = "old@test.com";
        String newEmail = "new@test.com";
        User user = createUser(oldEmail, Role.STUDENT);

        when(userRepository.findByEmail(oldEmail)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.updateUserEmail(oldEmail, newEmail);

        // then
        assertThat(result.getEmail()).isEqualTo(newEmail);
        verify(notificationService).sendAccountUpdateNotification(eq(newEmail), any());
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        // given
        List<User> users = Arrays.asList(
                createUser("test1@test.com", Role.STUDENT),
                createUser("test2@test.com", Role.EMPLOYEE)
        );
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> result = userService.getAllUsers();

        // then
        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // given
        String email = "test@test.com";
        User user = createUser(email, Role.STUDENT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        User result = userService.findByEmail(email);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        String email = "nonexistent@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> userService.findByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldResetPassword() {
        // given
        String email = "test@test.com";
        String newPassword = "newPassword123";
        User user = createUser(email, Role.STUDENT);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordManagementService.generateSecurePassword()).thenReturn(newPassword);
        when(passwordManagementService.encodePassword(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.resetPassword(email);

        // then
        assertThat(result).isNotNull();
        verify(notificationService).sendPasswordResetMessage(email, newPassword);
        verify(userRepository).save(user);
    }

    private User createUser(String email, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        user.setPassword("encodedPassword");
        return user;
    }
}