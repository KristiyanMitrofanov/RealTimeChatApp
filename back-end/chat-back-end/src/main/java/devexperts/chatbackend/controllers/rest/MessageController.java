package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.DTOs.MessageDTO;
import devexperts.chatbackend.exceptions.ValidationException;
import devexperts.chatbackend.mappers.MessageMapper;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import devexperts.chatbackend.services.contracts.ChatService;
import devexperts.chatbackend.services.contracts.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats/{chatId}/messages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Messages")
public class MessageController {

    public static final String OK_MESSAGE = "You have successfully created the message!";

    private final MessageService service;

    private final MessageMapper mapper;

    private final ChatService chatService;

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Retrieve messages for chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Message.class)))}),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public List<MessageDTO> getMessages(@PathVariable long chatId, Authentication authentication) {
        Chat chat = chatService.getById(chatId, authentication);
        return service.getMessages(chat).stream()
                .map(e -> new MessageDTO(e.getCreator().getUsername(), e.getContent(), e.getTimestamp()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Create message in chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message created successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Message.class)))}),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public ResponseEntity<String> createMessage(@Valid @RequestBody MessageDTO message, Errors errors,
                                                Authentication authentication, @PathVariable long chatId) {
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors()) {
                throw new ValidationException(errors.getFieldError().getDefaultMessage());
            }
        }

        Chat chat = chatService.getById(chatId, authentication);
        Message mappedMessage = mapper.fromDto(message, authentication, chat);
        service.createMessage(mappedMessage, authentication, chat);
        return ResponseEntity.status(HttpStatus.OK).body(OK_MESSAGE);
    }
}
