package devexperts.chatbackend.services.contracts;

import devexperts.chatbackend.models.LoginRequest;
import devexperts.chatbackend.models.LoginResponse;
import devexperts.chatbackend.models.User;

public interface UserService {

    User getById(long id);

    void create(User userToRegister);

    User getByUsername(String username);

    LoginResponse authenticate(LoginRequest request);
}
