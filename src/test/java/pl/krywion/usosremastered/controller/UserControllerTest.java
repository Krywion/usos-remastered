package pl.krywion.usosremastered.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.krywion.usosremastered.config.security.Role;
import pl.krywion.usosremastered.dto.domain.UserDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = createUser();
        userDto = createUserDto();
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("Should get authenticated user")
    @Test
    void shouldGetAuthenticatedUser() {
        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // when
        ResponseEntity<ServiceResponse<UserDto>> response = userController.authenticatedUser();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(userDto);
        assertThat(response.getBody().getMessage()).isEqualTo("User found successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
    }

    @DisplayName("Should get all users")
    @Test
    void shouldGetAllUsers() {
        // given
        List<User> users = Arrays.asList(user, user);

        when(userService.getAllUsers()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);

        // when
        ResponseEntity<ServiceResponse<List<UserDto>>> response = userController.allUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2);
        assertThat(response.getBody().getMessage()).isEqualTo("Users found successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
        verify(userService).getAllUsers();
    }

    @DisplayName("Should reset password")
    @Test
    void shouldResetPassword() {
        // given
        String email = "test@example.com";
        when(userService.resetPassword(email)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // when
        ResponseEntity<ServiceResponse<UserDto>> response = userController.resetPassword(email);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(userDto);
        assertThat(response.getBody().getMessage()).isEqualTo("Password reset successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
        verify(userService).resetPassword(email);
    }

    @DisplayName("Should return empty list when no users")
    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        // given
        when(userService.getAllUsers()).thenReturn(List.of());

        // when
        ResponseEntity<ServiceResponse<List<UserDto>>> response = userController.allUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEmpty();
        assertThat(response.getBody().getMessage()).isEqualTo("Users found successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole(Role.ADMIN);
        return user;
    }

    private UserDto createUserDto() {
        return new UserDto(1L, "test@example.com", "ADMIN");
    }
}