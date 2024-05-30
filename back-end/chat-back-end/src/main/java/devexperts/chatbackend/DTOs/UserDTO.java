package devexperts.chatbackend.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    @NotEmpty(message = "You must fill username!")
    @Size(max = 30, message = "The username cannot be longer than 30 characters!")
    private String username;

    @NotEmpty(message = "You must fill email!")
    @Email(message = "You must provide a valid email!")
    private String email;

    @NotEmpty(message = "You must fill password!")
    private String password;

    @NotEmpty(message = "You must confirm your password!")
    private String confirmPassword;
}
