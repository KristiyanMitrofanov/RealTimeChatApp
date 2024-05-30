package devexperts.chatbackend.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "You must fill your username!")
    private String username;

    @NotEmpty(message = "You must fill your password!")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
