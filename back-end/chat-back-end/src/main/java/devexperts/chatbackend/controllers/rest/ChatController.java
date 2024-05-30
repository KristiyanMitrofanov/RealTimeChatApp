package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.DTOs.CreateChatDTO;
import devexperts.chatbackend.DTOs.SendChatDTO;
import devexperts.chatbackend.exceptions.ValidationException;
import devexperts.chatbackend.filters.enums.ChatSortField;
import devexperts.chatbackend.mappers.ChatMapper;
import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.services.contracts.ChatService;
import devexperts.chatbackend.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Chats")
public class ChatController {

    public static final String DELETE_CHAT_MESSAGE = "Chat was successfully deleted!";
    public static final String NO_USERNAME = "You must provide a username!";
    private final ChatService service;

    private final UserService userService;

    private final ChatMapper mapper;
    public static final String ADD_MESSAGE = "User %s was added to the chat!";
    public static final String DELETE_USER_MESSAGE = "User removed from the chat!";

    @GetMapping("/chats")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Retrieve all chats", parameters = {
            @Parameter(name = "name"),
            @Parameter(name = "username"),
            @Parameter(name = "page"),
            @Parameter(name = "sizePerPage"),
            @Parameter(name = "sortField"),
            @Parameter(name = "sortDirection")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chats retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Chat.class)))}),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public Page<SendChatDTO> getAll(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String username,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int sizePerPage,
                                    @RequestParam(defaultValue = "ID") ChatSortField sortField,
                                    @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());
        return service.getAll(username, name, pageable).map((chat) -> new SendChatDTO(chat.getId(), chat.getName()));
    }

    @GetMapping("/chats/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Retrieve chat by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat retrieved successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Chat.class))}),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public Chat getById(@PathVariable long id, Authentication authentication) {
        return service.getById(id, authentication);
    }

    @PostMapping("/chats")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Create a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat created successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "406", description = "Duplicated chat", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public Chat create(@Valid @RequestBody CreateChatDTO dto, Errors errors, Authentication authentication) {
        if (errors.hasFieldErrors()) {
            throw new ValidationException(errors.getFieldError().getDefaultMessage());
        }

        Chat chat = mapper.fromDto(dto);
        return service.create(chat, authentication);
    }

    @PostMapping("/chats/{chatId}/users")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Add user to a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "406", description = "Duplicated user", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public ResponseEntity addUserToChat(@PathVariable long chatId, Authentication authentication, @RequestParam String username) {
        if (username.isEmpty()) {
            throw new ValidationException(NO_USERNAME);
        }
        Chat chat = service.getById(chatId, authentication);
        User user = userService.getByUsername(username);
        service.addUserToChat(chat, user);
        return ResponseEntity.status(HttpStatus.OK).body(String.format(ADD_MESSAGE, user.getUsername()));
    }

    @DeleteMapping("/chats/{chatId}/users")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Delete user from chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public ResponseEntity deleteUserFromChat(@PathVariable long chatId, Authentication authentication, @RequestParam String username) {
        Chat chat = service.getById(chatId, authentication);
        User user = userService.getByUsername(username);
        service.deleteUserFromChat(chat, user);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_USER_MESSAGE);
    }

    @DeleteMapping("/chats/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Delete a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public ResponseEntity deleteChat(@PathVariable long id, Authentication authentication) {
        service.delete(id, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_CHAT_MESSAGE);
    }
}
