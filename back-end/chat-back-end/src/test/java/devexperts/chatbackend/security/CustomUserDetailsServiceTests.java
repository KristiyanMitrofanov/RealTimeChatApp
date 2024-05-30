package devexperts.chatbackend.security;

import devexperts.chatbackend.Helpers;
import devexperts.chatbackend.exceptions.EntityNotFoundException;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {

    @Mock
    UserRepository repository;

    @InjectMocks
    CustomUserDetailsService service;

    @Test
    public void loadByUsername_ShouldReturnUser_IfUserExistsInDatabase() {

        //Arrange
        Optional<User> user = Helpers.createMockUser();
        Mockito.when(repository.findUserByUsername(Mockito.anyString())).thenReturn(user);

        //Act
        UserDetails result = service.loadUserByUsername("mockUsername");

        //Assert
        Assertions.assertEquals(user.get(), result);
    }

    @Test
    public void loadByUsername_ShouldThrow_IfUserNotFound() {
        //Arrange
        Mockito.when(repository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.loadUserByUsername("mockUsername"));
    }
}
