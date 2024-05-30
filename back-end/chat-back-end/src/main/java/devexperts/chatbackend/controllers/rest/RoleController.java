package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.DTOs.RoleDTO;
import devexperts.chatbackend.exceptions.ValidationException;
import devexperts.chatbackend.mappers.RoleMapper;
import devexperts.chatbackend.models.Role;
import devexperts.chatbackend.services.contracts.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@CrossOrigin("*")
@Tag(name = "Roles")
public class RoleController {

    private final RoleService service;

    private final RoleMapper mapper;
    private static final String OK_MESSAGE = "Role was created successfully!";

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Create a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role created successfully"),
            @ApiResponse(responseCode = "406", description = "Duplicated user", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Invalid Token", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))})
    public ResponseEntity createRole(@Valid @RequestBody RoleDTO dto, Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new ValidationException(errors.getFieldError().getDefaultMessage());
        }
        Role role = mapper.fromDto(dto);
        service.create(role);
        return ResponseEntity.status(HttpStatus.OK).body(OK_MESSAGE);
    }
}
