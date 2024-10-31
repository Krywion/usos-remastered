package pl.krywion.usosremastered.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String role;

    public UserDto(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UserDto() {
    }
}
