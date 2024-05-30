package devexperts.chatbackend.services.contracts;

import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ChatService {

    Chat create(Chat chat, Authentication authentication);

    void addUserToChat(Chat chat, User user);

    void deleteUserFromChat(Chat chat, User user);

    Page<Chat> getAll(String username, String name, Pageable pageable);

    Chat getById(long id, Authentication authentication);

    void delete(long id, Authentication authentication);
}
