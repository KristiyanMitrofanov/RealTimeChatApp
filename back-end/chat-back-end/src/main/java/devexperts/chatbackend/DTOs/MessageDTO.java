package devexperts.chatbackend.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO {

    private String content;

    @Schema(hidden = true)
    private String creator;

    @Schema(hidden = true)
    private String timestamp;
}
