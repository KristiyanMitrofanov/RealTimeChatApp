package devexperts.chatbackend.services.implementations;

import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.models.Role;
import devexperts.chatbackend.repositories.RoleRepository;
import devexperts.chatbackend.services.contracts.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public void create(Role role) {
        checkNameUniqueness(role.getName());
        repository.save(role);
    }

    private void checkNameUniqueness(String name) {
        Optional<Role> roleFromRepository = repository.findByName(name);
        if (roleFromRepository.isPresent()) {
            throw new DuplicateEntityException("Role", "name", name);
        }
    }


}
