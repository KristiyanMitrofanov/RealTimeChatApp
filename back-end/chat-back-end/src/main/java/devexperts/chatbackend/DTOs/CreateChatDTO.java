package devexperts.chatbackend.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateChatDTO {

    @Size(min = 3, max = 30, message = "Chat name must be between 3 and 30 characters long!")
    @NotEmpty(message = "Chat name cannot be empty!")
    private String name;
}
