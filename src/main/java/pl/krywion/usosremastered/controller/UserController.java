package pl.krywion.usosremastered.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.UserDto;
import pl.krywion.usosremastered.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDto currentUser = (UserDto) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> users = userService.allUsers().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }


}
