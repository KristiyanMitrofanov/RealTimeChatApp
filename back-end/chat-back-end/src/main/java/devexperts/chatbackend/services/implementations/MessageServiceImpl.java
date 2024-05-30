package devexperts.chatbackend.services.implementations;

import devexperts.chatbackend.enums.UserRoles;
import devexperts.chatbackend.exceptions.UnauthorizedOperationException;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import devexperts.chatbackend.repositories.MessageRepository;
import devexperts.chatbackend.services.contracts.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    public static final String UNAUTHORIZED_MSG = "You are not authorized to access this chat room!";

    private final MessageRepository repository;

    @Override
    public List<Message> getMessages(Chat chat) {
        return repository.getMessages(chat);
    }

    @Override
    public Message createMessage(Message message, Authentication authentication, Chat chat) {
        checkUserPermission(authentication, Optional.of(chat));
        return repository.save(message);
    }

    private static boolean isLoggedUserAddedToChat(Authentication authentication, Optional<Chat> chat) {
        return chat.get().getUsers().stream()
                .anyMatch(u -> u.getUsername().equals(authentication.getName()));
    }

    private static void checkUserPermission(Authentication authentication, Optional<Chat> chat) {
        boolean isAdded = isLoggedUserAddedToChat(authentication, chat);
        if (!isAdded && !authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.ADMIN.toString()))) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MSG);
        }
    }

}
