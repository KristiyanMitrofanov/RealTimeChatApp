package devexperts.chatbackend.controllers.rest;

import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.exceptions.EntityNotFoundException;
import devexperts.chatbackend.exceptions.UnauthorizedOperationException;
import devexperts.chatbackend.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    public static final String BAD_CREDENTIALS_MSG = "Invalid username or password";

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity handleDuplicateEntityException(DuplicateEntityException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_CREDENTIALS_MSG);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity handleUnauthorizedOperationException(UnauthorizedOperationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity handleUnauthorizedOperationException(UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
