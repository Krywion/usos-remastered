package pl.krywion.usosremastered.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.krywion.usosremastered.dto.domain.UserDto;
import pl.krywion.usosremastered.dto.response.ServiceResponse;
import pl.krywion.usosremastered.entity.User;
import pl.krywion.usosremastered.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;



    @GetMapping("/me")
    @Operation(
            summary = "Get authenticated user",
            description = "Get the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    public ResponseEntity<ServiceResponse<UserDto>> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        UserDto currentUserDto = modelMapper.map(currentUser, UserDto.class);

        return ResponseEntity.ok(
                ServiceResponse.success(currentUserDto,
                        "User found successfully",
                        HttpStatus.OK)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Get all users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })

    public ResponseEntity<ServiceResponse<List<UserDto>>> allUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ServiceResponse.success(userDtos,
                        "Users found successfully",
                        HttpStatus.OK)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reset")
    @Operation(
            summary = "Reset user password",
            description = "Reset user password by email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public ResponseEntity<ServiceResponse<UserDto>> resetPassword(@RequestParam String email) {
        User user = userService.resetPassword(email);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return ResponseEntity.ok(
                ServiceResponse.success(userDto,
                        "Password reset successfully",
                        HttpStatus.OK)
        );
    }



}
