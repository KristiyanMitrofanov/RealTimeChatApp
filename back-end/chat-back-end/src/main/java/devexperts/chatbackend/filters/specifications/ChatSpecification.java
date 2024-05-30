package devexperts.chatbackend.filters.specifications;

import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ChatSpecification {

    private ChatSpecification() {

    }

    public static Specification<Chat> username(String username) {
        return (root, query, builder) -> {
            Join<Chat, User> usersJoin = root.join("users");
            return builder.equal(usersJoin.get("username"), username);
        };
    }

    public static Specification<Chat> name(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
}
