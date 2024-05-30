package devexperts.chatbackend;

import devexperts.chatbackend.models.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Helpers {

    public static Message createMockMessage() {
        return new Message(2L, "Mock Message", "19/04/2024", Helpers.createMockUser().get(), new Chat());
    }

    public static Chat createMockChat() {
        Chat chat = new Chat();
        chat.setName("mockName");
        chat.setId(1L);
        chat.setHistory(List.of(createMockMessage()));
        Set<User> users = new HashSet<>();
        users.add(createMockUser().get());
        chat.setUsers(users);
        return chat;
    }

    public static Role createMockRole() {
        Role role = new Role();
        role.setName("mockName");
        role.setId(1L);
        return role;
    }

    public static Optional<User> createMockUser() {
        return Optional.of(new User(3L, "MockUsername", "MockEmail", "MockPassword", createMockRole()));
    }


    public static LoginRequest createMockLoginRequest() {
        return new LoginRequest("mockUsername", "mockPassword");
    }
}
