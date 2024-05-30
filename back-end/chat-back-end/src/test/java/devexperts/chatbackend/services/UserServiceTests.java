package devexperts.chatbackend.services;

import devexperts.chatbackend.Helpers;
import devexperts.chatbackend.exceptions.DuplicateEntityException;
import devexperts.chatbackend.models.LoginRequest;
import devexperts.chatbackend.models.LoginResponse;
import devexperts.chatbackend.models.User;
import devexperts.chatbackend.repositories.UserRepository;
import devexperts.chatbackend.security.JWTUtil;
import devexperts.chatbackend.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository repository;

    @Mock
    Authentication authentication;

    @InjectMocks
    UserServiceImpl service;

    private MockedStatic<JWTUtil> mockedStatic;

    @BeforeEach
    public void setUp() {
        mockedStatic = mockStatic(JWTUtil.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
    }

    @Test
    public void create_Should_ThrowIfUsernameTaken() {
        //Arrange
        User user = Helpers.createMockUser().get();
        Mockito.when(repository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_Should_ThrowIfEmailTaken() {
        //Arrange
        User user = Helpers.createMockUser().get();
        Mockito.when(repository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_Should_CallRepositoryToSaveUser() {
        //Arrange
        Optional<User> mockUser = Helpers.createMockUser();
        //Act
        service.create(mockUser.get());

        //Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository, Mockito.times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(mockUser.get().getId(), savedUser.getId());
    }

    @Test
    public void authenticate_Should_ReturnCorrectResponse() {
        //Arrange
        String mockToken = "mockToken";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        LoginRequest mockLoginRequest = Helpers.createMockLoginRequest();
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mockLoginRequest.getUsername(), mockLoginRequest.getPassword()))).thenReturn(authentication);
        mockedStatic.when(() -> JWTUtil.createToken(mockLoginRequest.getUsername(), authorities)).thenReturn(mockToken);
        Mockito.when(authentication.getAuthorities()).thenAnswer(a -> authorities);
        Mockito.when(authentication.getName()).thenReturn(mockLoginRequest.getUsername());
        //Act
        LoginResponse response = service.authenticate(mockLoginRequest);

        //Assert
        Assertions.assertEquals(mockLoginRequest.getUsername(), response.getUsername());
    }

    @Test
    public void authenticate_Should_ThrowIfUserProvidesWrongCredentials() {
        //Arrange
        LoginRequest request = Helpers.createMockLoginRequest();
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

        //Act & Assert
        Assertions.assertThrows(BadCredentialsException.class, () -> service.authenticate(request));

    }

    @Test
    public void authenticate_Should_ReturnTokenInTheResponse() {
        //Arrange
        String mockToken = "mockToken";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        LoginRequest request = Helpers.createMockLoginRequest();
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))).thenReturn(authentication);
        mockedStatic.when(() -> JWTUtil.createToken(request.getUsername(), authorities)).thenReturn(mockToken);
        Mockito.when(authentication.getAuthorities()).thenAnswer(a -> authorities);
        Mockito.when(authentication.getName()).thenReturn(request.getUsername());
        //Act
        LoginResponse response = service.authenticate(request);

        //Assert
        Assertions.assertEquals(mockToken, response.getToken());
    }
}
