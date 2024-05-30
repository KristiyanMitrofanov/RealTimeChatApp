package devexperts.chatbackend.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    @Size(max = 20, message = "The role name cannot exceed 20 characters!")
    @NotEmpty(message = "You must fill the role name!")
    @NotBlank(message = "Role name cannot be blank!")
    private String name;
}
