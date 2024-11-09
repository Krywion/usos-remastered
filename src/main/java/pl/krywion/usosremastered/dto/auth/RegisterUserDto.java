package pl.krywion.usosremastered.dto.auth;

import lombok.Getter;
import lombok.Setter;
import pl.krywion.usosremastered.config.security.Role;

@Setter
@Getter
public class RegisterUserDto {

    private String email;
    private String password;
    private Role role;

    public RegisterUserDto() {
    }


}
