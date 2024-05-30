package devexperts.chatbackend.mappers;

import devexperts.chatbackend.DTOs.UserDTO;
import devexperts.chatbackend.enums.UserRoles;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public User fromDto(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setRole(roleRepository.findByName(UserRoles.USER.toString()).get());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        return user;
    }
}
