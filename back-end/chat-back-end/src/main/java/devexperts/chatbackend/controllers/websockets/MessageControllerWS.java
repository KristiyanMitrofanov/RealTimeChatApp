package devexperts.chatbackend.controllers.websockets;

import devexperts.chatbackend.DTOs.MessageDTO;
import devexperts.chatbackend.mappers.MessageMapper;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import devexperts.chatbackend.models.Notification;
import devexperts.chatbackend.services.contracts.ChatService;
import devexperts.chatbackend.services.contracts.MessageService;
import devexperts.chatbackend.services.contracts.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Controller
public class MessageControllerWS {

    private final MessageService service;

    private final MessageMapper mapper;

    private final ChatService chatService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationService notificationService;

    @MessageMapping("/chats/{chatId}/messages")
    public Chat createMessage(@Payload MessageDTO message,
                              Authentication authentication, @DestinationVariable long chatId) {
        Chat chat = chatService.getById(chatId, authentication);
        Message mappedMessage = mapper.fromDto(message, authentication, chat);
        service.createMessage(mappedMessage, authentication, chat);
        Chat updatedChat = chatService.getById(chatId, authentication);
        notificationService.sendNotification(chatId, new Notification(mappedMessage.getCreator().getUsername(), mappedMessage.getContent(), chatId));
        simpMessagingTemplate.convertAndSend("/topic/chats/" + chatId, updatedChat);
        return chat;
    }
}
