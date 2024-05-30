package devexperts.chatbackend.mappers;

import devexperts.chatbackend.DTOs.CreateChatDTO;
import devexperts.chatbackend.models.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {


    public Chat fromDto(CreateChatDTO dto) {
        Chat chat = new Chat();
        chat.setName(dto.getName());
        return chat;
    }
}
