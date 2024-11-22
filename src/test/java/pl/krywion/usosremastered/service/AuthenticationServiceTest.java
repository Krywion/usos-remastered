package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.auth.LoginUserDto;
import pl.krywion.usosremastered.dto.auth.RegisterUserDto;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.exception.ResourceNotFoundException;
import pl.krywion.usosremastered.repository.UserRepository;
import pl.krywion.usosremastered.service.common.AuthenticationService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication service tests")
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordManagementService passwordManagementService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private NotificationService notificationService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                userRepository,
                passwordManagementService,
                authenticationManager,
                notificationService
        );
    }

    @Test
    @DisplayName("Should sign up user with provided password")
    void shouldSignUpUserWithProvidedPassword() {
        // given
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setRole(Role.STUDENT);

        when(passwordManagementService.encodePassword("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // when
        User result = authenticationService.signUp(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getRole()).isEqualTo(dto.getRole());
        verify(notificationService).sendWelcomeMessage(dto.getEmail(), dto.getPassword());
    }

    @Test
    @DisplayName("Should sign up user with generated password")
    void shouldSignUpUserWithGeneratedPassword() {
        // given
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        dto.setRole(Role.STUDENT);

        String generatedPassword = "generated123";
        when(passwordManagementService.generateSecurePassword()).thenReturn(generatedPassword);
        when(passwordManagementService.encodePassword(generatedPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // when
        User result = authenticationService.signUp(dto);

        // then
        assertThat(result).isNotNull();
        verify(notificationService).sendWelcomeMessage(dto.getEmail(), generatedPassword);
    }

    @Test
    @DisplayName("Should authenticate user")
    void shouldAuthenticateUser() {
        // given
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password123");

        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword("encodedPassword");
        user.setRole(Role.STUDENT);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));

        // when
        User result = authenticationService.authenticate(loginDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(loginDto.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should throw exception when authenticating non-existent user")
    void shouldThrowExceptionWhenAuthenticatingNonExistentUser() {
        // given
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("nonexistent@example.com");
        loginDto.setPassword("password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> authenticationService.authenticate(loginDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("Should throw exception when authenticating with invalid credentials")
    void shouldThrowExceptionWhenAuthenticatingWithInvalidCredentials() {
        // given
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // when/then
        assertThatThrownBy(() -> authenticationService.authenticate(loginDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid username or password");

        verify(userRepository, never()).findByEmail(anyString());
    }
}