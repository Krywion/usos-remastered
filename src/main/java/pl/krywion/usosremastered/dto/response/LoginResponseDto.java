package pl.krywion.usosremastered.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDto {
    private String token;
    private Long expiresIn;

    public LoginResponseDto() {
    }

}
