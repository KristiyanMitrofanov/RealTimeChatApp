package devexperts.chatbackend.services.contracts;

import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService {

    List<Message> getMessages(Chat chat);

    Message createMessage(Message message, Authentication authentication, Chat chat);
}
