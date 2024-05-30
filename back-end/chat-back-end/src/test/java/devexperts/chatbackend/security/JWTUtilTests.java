package devexperts.chatbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@PropertySource("classpath:application.properties")
public class JWTUtilTests {

    @Mock
    HttpServletRequest request;

    private String token;


    @BeforeEach
    public void setUp() {
        JWTUtil.setSecret("This is my secret key for the JWT token");
        token = JWTUtil.createToken("mockUsername", List.of(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void createToken_ShouldReturnToken() {

        //Assert
        Assertions.assertNotNull(token);
    }

    @Test
    public void createToken_ShouldHave_CorrectSubject() {
        //Act
        Claims claims = Jwts.parser()
                .setSigningKey(JWTUtil.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        Assertions.assertEquals("mockUsername", claims.getSubject(), "Expected subject to match the username");
    }

    @Test
    public void resolveClaims_ShouldReturn_NullIfNoTokenProvided() {
        //Arrange
        Mockito.when(JWTUtil.resolveToken(request)).thenReturn(null);

        //Act
        Claims claims = JWTUtil.resolveClaims(request);

        //Assert
        Assertions.assertNull(claims);
    }

    @Test
    public void resolveToken_ShouldReturnToken_IfProvidedInHeader() {
        //Arrange
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        //Act
        String result = JWTUtil.resolveToken(request);

        //Assert
        Assertions.assertEquals(token, result);
    }

    @Test
    public void resolveToken_ShouldReturnNull_IfIncorrectTokenProvided() {
        //Arrange
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);

        //Act
        String result = JWTUtil.resolveToken(request);

        //Assert
        Assertions.assertNotEquals(token, result);
    }


}
