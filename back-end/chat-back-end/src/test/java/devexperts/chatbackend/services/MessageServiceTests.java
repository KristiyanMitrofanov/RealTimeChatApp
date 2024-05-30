package devexperts.chatbackend.services;

import devexperts.chatbackend.Helpers;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.MessageRepository;
import devexperts.chatbackend.services.implementations.MessageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {


    @Mock
    MessageRepository repository;

    @InjectMocks
    MessageServiceImpl service;

    @Mock
    Authentication authentication;

    @Test
    public void getMessages_Should_CallRepository() {

        //Arrange
        Chat chat = Helpers.createMockChat();

        //Act
        service.getMessages(chat);

        //Assert
        Mockito.verify(repository, Mockito.times(1)).getMessages(chat);
    }

    @Test
    public void createMessage_Should_CallRepositoryToSaveMessage() {
        // Arrange
        Chat mockChat = Helpers.createMockChat();
        User user = new User();
        user.setUsername("Mock Username");
        mockChat.setUsers(Set.of(user));
        Message mockMessage = Helpers.createMockMessage();
        Mockito.when(authentication.getName()).thenReturn("Mock Username");
//
        // Act
        service.createMessage(mockMessage, authentication, mockChat);

        // Assert
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(repository, Mockito.times(1)).save(messageCaptor.capture());
        Message savedMessage = messageCaptor.getValue();
        Assertions.assertEquals(mockMessage.getContent(), savedMessage.getContent());
    }


}
