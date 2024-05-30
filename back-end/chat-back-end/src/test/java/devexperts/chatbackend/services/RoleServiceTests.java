package devexperts.chatbackend.services;

import devexperts.chatbackend.Helpers;
import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.models.Role;
import devexperts.chatbackend.repositories.RoleRepository;
import devexperts.chatbackend.services.implementations.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    RoleRepository repository;

    @InjectMocks
    RoleServiceImpl service;

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        Role mockRole = Helpers.createMockRole();


        //Act
        service.create(mockRole);

        //Assert
        Mockito.verify(repository, Mockito.times(1)).save(mockRole);
    }

    @Test
    public void create_Should_Throw_IfNameDuplicated() {
        //Arrange
        Role role = Helpers.createMockRole();
        Mockito.when(repository.findByName(Mockito.any())).thenReturn(Optional.of(role));

        //Act && Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(role));
    }


}
