package devexperts.chatbackend.services;

import devexperts.chatbackend.Helpers;
import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.exceptions.EntityNotFoundException;
import devexperts.chatbackend.exceptions.UnauthorizedOperationException;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.ChatRepository;
import devexperts.chatbackend.services.implementations.ChatServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTests {

    @Mock
    ChatRepository repository;

    @InjectMocks
    ChatServiceImpl service;

    @Mock
    Authentication authentication;

    @Test
    public void getAll_Should_ReturnPageOfChats() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Chat> expectedPage = new PageImpl<>(List.of(new Chat()), pageable, 1);

        Mockito.when(repository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable))).thenReturn(expectedPage);

        Page<Chat> result = service.getAll("usernameExample", "nameExample", pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result);
        Mockito.verify(repository, Mockito.times(1)).findAll(Mockito.any(Specification.class), Mockito.eq(pageable));
    }

    @Test
    public void getById_ShouldReturnChatIfExistAndLoggedUserIsAdded() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(chat));
        Mockito.when(authentication.getName()).thenReturn("MockUsername");

        //Act
        Chat result = service.getById(1, authentication);

        //Assert
        Assertions.assertEquals(chat, result);
    }

    @Test
    public void getById_Should_Throw_IfChatDoesNotExist() {
        //Arrange
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        //Act && Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getById(1, authentication));
    }

    @Test
    public void getById_Should_Throw_IfUserIsNotAddedToChatOrAdmin() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(chat));
        Mockito.when(authentication.getName()).thenReturn("OtherUser");

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getById(1, authentication));
    }


    @Test
    public void create_Should_CallRepositoryToSaveUserIfValid() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findByName(chat.getName())).thenReturn(Optional.empty());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Mockito.when(authentication.getAuthorities()).thenAnswer(a -> authorities);
        //Act
        service.create(chat, authentication);

        //Assert
        Mockito.verify(repository, Mockito.times(1)).save(chat);
    }

    @Test
    public void create_Should_Throw_IfChatNameDuplicated() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findByName(chat.getName())).thenReturn(Optional.of(chat));

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(chat, authentication));
    }

    @Test
    public void create_Should_Throw_IfUserNotAdmin() {
        //Arrange
        Chat chat = Helpers.createMockChat();

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(chat, authentication));
    }

    @Test
    public void delete_Should_Throw_IfChatDoesNotExist() {
        //Arrange
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(1, authentication));
    }

    @Test
    public void delete_Should_Throw_IfUserNotAdmin() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(chat));

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(1, authentication));
    }

    @Test
    public void delete_Should_CallRepositoryToDeleteChat() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(chat));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Mockito.when(authentication.getAuthorities()).thenAnswer(a -> authorities);

        //Act
        service.delete(1, authentication);

        //Assert
        Mockito.verify(repository, Mockito.times(1)).delete(chat);
    }

    @Test
    public void addUserToChat_Should_Throw_IfUserIsAlreadyAddedToChat() {
        //Arrange
        User user = Helpers.createMockUser().get();
        Chat chat = Helpers.createMockChat();
        chat.setUsers(Set.of(user));

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.addUserToChat(chat, user));
    }

    @Test
    public void addUserToChat_Should_CallRepositoryToAddUser() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        Set<User> users = new HashSet<>();
        users.add(new User());
        chat.setUsers(users);
        User user = Helpers.createMockUser().get();
        //Act
        service.addUserToChat(chat, user);

        //Assert
        Mockito.verify(repository, Mockito.times(1)).save(chat);
    }

    @Test
    public void addUserToChat_Should_Throw_IfUserToAddIsAdmin() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        User user = Helpers.createMockUser().get();
        user.getRole().setName("ADMIN");
        //Act & Assert
        Assertions.assertThrows(UnsupportedOperationException.class, () -> service.addUserToChat(chat,user));
    }

    @Test
    public void deleteUserFromChat_Should_ThrowIfUserNotExistingInChat() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        chat.setUsers(Set.of());
        User user = Helpers.createMockUser().get();

        //Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.deleteUserFromChat(chat, user));
    }

    @Test
    public void deleteUserFromChat_Should_CallRepositoryToDeleteUser() {
        //Arrange
        Chat chat = Helpers.createMockChat();
        User user = Helpers.createMockUser().get();
        //Act
        service.deleteUserFromChat(chat, user);
        //Assert
        Mockito.verify(repository, Mockito.times(1)).save(chat);
    }


}
