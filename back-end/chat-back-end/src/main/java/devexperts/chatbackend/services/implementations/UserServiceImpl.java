package devexperts.chatbackend.services.implementations;

import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.exceptions.EntityNotFoundException;
import devexperts.chatbackend.models.LoginRequest;
import devexperts.chatbackend.models.LoginResponse;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.UserRepository;
import devexperts.chatbackend.security.JWTUtil;
import devexperts.chatbackend.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User with %s %s is not found!";
    private final UserRepository repository;

    private final AuthenticationManager authenticationManager;


    @Override
    public User getById(long id) {
        Optional<User> userFromRepository = repository.findById(id);
        if (userFromRepository.isEmpty()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND, "id", id));
        }
        return userFromRepository.get();
    }


    @Override
    public void create(User userToRegister) {
        checkUsernameUniqueness(userToRegister.getUsername());
        checkEmailUniqueness(userToRegister.getEmail());
        repository.save(userToRegister);
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> userFromRepository = repository.findUserByUsername(username);
        if (userFromRepository.isEmpty()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND, "username", username));
        }
        return userFromRepository.get();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = JWTUtil.createToken(authentication.getName(), (List<GrantedAuthority>) authentication.getAuthorities());
        String roleName = authentication.getAuthorities().toString().substring(6, authentication.getAuthorities().toString().length() - 1);
        return new LoginResponse(authentication.getName(), token, roleName);
    }


    private void checkUsernameUniqueness(String username) {
        Optional<User> userFromRepository = repository.findUserByUsername(username);
        if (userFromRepository.isPresent()) {
            throw new DuplicateEntityException("User", "username", username);
        }
    }

    private void checkEmailUniqueness(String email) {
        Optional<User> userFromRepository = repository.findUserByEmail(email);
        if (userFromRepository.isPresent()) {
            throw new DuplicateEntityException("User", "email", email);
        }
    }
}
