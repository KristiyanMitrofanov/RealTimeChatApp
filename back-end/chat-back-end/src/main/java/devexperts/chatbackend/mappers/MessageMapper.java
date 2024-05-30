package devexperts.chatbackend.mappers;

import devexperts.chatbackend.DTOs.MessageDTO;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import devexperts.chatbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss a";

    private final UserRepository userRepository;

    public Message fromDto(MessageDTO messageDTO, Authentication authentication, Chat chat) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setTimestamp(formatter.format(new Date()));
        message.setCreator(userRepository.findUserByUsername(authentication.getName()).get());
        message.setChat(chat);
        return message;
    }
}
