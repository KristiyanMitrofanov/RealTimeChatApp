package devexperts.chatbackend.services.implementations;

import devexperts.chatbackend.enums.UserRoles;
import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.exceptions.EntityNotFoundException;
import devexperts.chatbackend.exceptions.UnauthorizedOperationException;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.ChatRepository;
import devexperts.chatbackend.services.contracts.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

import static devexperts.chatbackend.filters.specifications.ChatSpecification.name;
import static devexperts.chatbackend.filters.specifications.ChatSpecification.username;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    public static final String CHAT_NOT_FOUND_MSG = "Chat with id %d not found!";
    public static final String ALREADY_ADDED_MSG = "This user is already added to the chat room!";
    public static final String ALREADY_REMOVED_MSG = "User to remove is not existing in this chat room!";
    public static final String UNAUTHORIZED_MSG = "You are not authorized to perform this operation!";
    public static final String NO_ADMINS_MSG = "You cannot add admins to the chat!";

    private final ChatRepository repository;

    @Override
    public Page<Chat> getAll(String usernameField, String nameField, Pageable pageable) {
        Specification<Chat> filter = Specification
                .where(StringUtils.isEmptyOrWhitespace(usernameField)
                        ? null : username(usernameField).and(StringUtils.isEmptyOrWhitespace(nameField) ? null : name(nameField)));

        return repository.findAll(filter, pageable);
    }

    @Override
    public Chat getById(long id, Authentication authentication) {
        Optional<Chat> chat = checkChatExistence(id);
        checkUserPermission(authentication, chat);
        return chat.get();
    }

    @Override
    public Chat create(Chat chat, Authentication authentication) {
        checkNameUniqueness(chat.getName());
        checkUserPermission(authentication, Optional.of(chat));
        return repository.save(chat);
    }

    @Override
    public void delete(long id, Authentication authentication) {
        Chat chat = getById(id, authentication);
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MSG);
        }
        repository.delete(chat);
    }

    @Override
    public void addUserToChat(Chat chat, User user) {
        if (chat.getUsers().contains(user)) {
            throw new DuplicateEntityException(ALREADY_ADDED_MSG);
        } else if (user.getRole().getName().equals(UserRoles.ADMIN.toString())) {
            throw new UnsupportedOperationException(NO_ADMINS_MSG);
        }
        chat.getUsers().add(user);
        repository.save(chat);
    }

    @Override
    public void deleteUserFromChat(Chat chat, User user) {
        if (!chat.getUsers().contains(user)) {
            throw new EntityNotFoundException(ALREADY_REMOVED_MSG);
        }
        chat.getUsers().remove(user);
        repository.save(chat);
    }

    private static void checkUserPermission(Authentication authentication, Optional<Chat> chat) {

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !isLoggedUserAddedToChat(authentication, chat)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MSG);
        }
    }

    private Optional<Chat> checkChatExistence(long id) {
        Optional<Chat> chat = repository.findById(id);
        if (chat.isEmpty()) {
            throw new EntityNotFoundException(String.format(CHAT_NOT_FOUND_MSG, id));
        }
        return chat;
    }

    private static boolean isLoggedUserAddedToChat(Authentication authentication, Optional<Chat> chat) {
        return chat.get().getUsers().stream()
                .anyMatch(u -> u.getUsername().equals(authentication.getName()));
    }

    private void checkNameUniqueness(String name) {
        Optional<Chat> chatFromRepository = repository.findByName(name);
        if (chatFromRepository.isPresent()) {
            throw new DuplicateEntityException("Chat", "name", name);
        }
    }


}
