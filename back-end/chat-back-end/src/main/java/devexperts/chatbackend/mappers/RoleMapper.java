package devexperts.chatbackend.mappers;

import devexperts.chatbackend.DTOs.RoleDTO;
import devexperts.chatbackend.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role fromDto(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        return role;
    }
}
