package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.DTOs.UserDTO;
import devexperts.chatbackend.exceptions.ValidationException;
import devexperts.chatbackend.mappers.UserMapper;
import devexperts.chatbackend.models.LoginRequest;
import devexperts.chatbackend.models.LoginResponse;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    public static final String PASS_MISMATCH = "Passwords must match!";
    public static final String SUCCESS = "User was registered successfully!";

    private final UserMapper mapper;

    private final UserService service;

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully!",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(example = "You must fill your username!"))})})
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginReq, Errors errors) {
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors()) {
                throw new ValidationException(errors.getFieldError().getDefaultMessage());
            }
        }
        LoginResponse response = service.authenticate(loginReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(example = "User registered successfully!"))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(example = "The message cannot be empty!"))}),
            @ApiResponse(responseCode = "409", description = "User data already exists",
                    content = {@Content(mediaType = "application/json", schema = @Schema(example = "User with this email already exists!"))})})
    public ResponseEntity register(@Valid @RequestBody UserDTO dto, Errors errors) {
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors()) {
                throw new ValidationException(errors.getFieldError().getDefaultMessage());
            }
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ValidationException(PASS_MISMATCH);
        }
        User userToRegister = mapper.fromDto(dto);
        service.create(userToRegister);
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS);
    }
}
