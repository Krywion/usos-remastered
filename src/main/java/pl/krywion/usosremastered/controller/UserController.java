package pl.krywion.usosremastered.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.domain.UserDto;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.service.impl.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final ModelMapper modelMapper;

    public UserController(
            UserServiceImpl userServiceImpl,
            ModelMapper modelMapper
    ) {
        this.userServiceImpl = userServiceImpl;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        UserDto currentUserDto = modelMapper.map(currentUser, UserDto.class);

        return ResponseEntity.ok(currentUserDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> users = userServiceImpl.allUsers().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getRole().name()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }



}
