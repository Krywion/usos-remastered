package pl.krywion.usosremastered.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.service.common.JwtService;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Service Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expirationTime", EXPIRATION_TIME);
    }

    @Test
    @DisplayName("Should generate token for user")
    void shouldGenerateTokenForUser() {
        // given
        UserDetails userDetails = createUser("test@example.com");

        // when
        String token = jwtService.generateToken(userDetails);

        // then
        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtService.extractUsername(token)).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("Should generate token with extra claims for user")
    void shouldGenerateTokenWithExtraClaimsForUser() {
        // given
        UserDetails userDetails = createUser("test@example.com");
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        // when
        String token = jwtService.generateToken(extraClaims, userDetails);

        // then
        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtService.extractUsername(token)).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("Should validate valid token")
    void shouldValidateValidToken() {
        // given
        UserDetails userDetails = createUser("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should not validate token with different user")
    void shouldNotValidateTokenWithDifferentUser() {
        // given
        UserDetails userDetails1 = createUser("test1@example.com");
        UserDetails userDetails2 = createUser("test2@example.com");
        String token = jwtService.generateToken(userDetails1);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails2);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should not validate expired token")
    void shouldNotValidateExpiredToken() {
        // given
        UserDetails userDetails = createUser("test@example.com");
        ReflectionTestUtils.setField(jwtService, "expirationTime", -1000L);
        String token = jwtService.generateToken(userDetails);

        // when/then
        assertThatThrownBy(() -> jwtService.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("Should extract all claims")
    void shouldExtractAllClaims() {
        // given
        UserDetails userDetails = createUser("test@example.com");
        String token = jwtService.generateToken(userDetails);

        // when
        String claims = jwtService.extractClaim(token, Claims::getSubject);

        // then
        assertThat(claims).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("Should throw exception for invalid token")
    void shouldThrowExceptionForInvalidToken() {
        // given
        String invalidToken = "invalid.token.signature";

        // when/then
        assertThatThrownBy(() -> jwtService.extractUsername(invalidToken))
                .isInstanceOf(MalformedJwtException.class)
                .hasMessageContaining("Malformed JWT");
    }

    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.ADMIN);
        return user;
    }
}