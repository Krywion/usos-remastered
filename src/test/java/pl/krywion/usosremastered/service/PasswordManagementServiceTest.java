package pl.krywion.usosremastered.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.krywion.usosremastered.service.impl.PasswordManagementServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Password Management Service Tests")
class PasswordManagementServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private PasswordManagementServiceImpl passwordManagementService;

    @BeforeEach
    void setUp() {
        passwordManagementService = new PasswordManagementServiceImpl(passwordEncoder);
    }

    @Test
    @DisplayName("Should generate secure password")
    void shouldGenerateSecurePassword() {
        // when
        String password = passwordManagementService.generateSecurePassword();

        // then
        assertThat(password)
                .hasSize(12)
                .matches(".*[A-Z].*")
                .matches(".*[a-z].*")
                .matches(".*\\d.*")
                .matches(".*[!@#$%^&*()-+].*");
    }

    @Test
    @DisplayName("Should generate unique passwords")
    void shouldGenerateUniquePasswords() {
        // when
        String password1 = passwordManagementService.generateSecurePassword();
        String password2 = passwordManagementService.generateSecurePassword();

        // then
        assertThat(password1).isNotEqualTo(password2);
    }

    @Test
    @DisplayName("Should encode password")
    void shouldEncodePassword() {
        // given
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        // when
        String result = passwordManagementService.encodePassword(rawPassword);

        // then
        assertThat(result).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("Should generate password with required length")
    void shouldGeneratePasswordWithRequiredLength() {
        // when
        String password = passwordManagementService.generateSecurePassword();

        // then
        assertThat(password).hasSize(12);
    }

    @Test
    @DisplayName("Should generate password with all required character types")
    void shouldGeneratePasswordWithAllRequiredCharacterTypes() {
        // when
        String password = passwordManagementService.generateSecurePassword();

        // then
        assertThat(password)
                .containsPattern("[A-Z]")
                .containsPattern("[a-z]")
                .containsPattern("\\d")
                .containsPattern("[!@#$%^&*()-+]");
    }
}