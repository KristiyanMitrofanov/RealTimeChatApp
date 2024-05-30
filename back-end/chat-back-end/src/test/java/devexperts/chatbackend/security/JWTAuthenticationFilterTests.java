package devexperts.chatbackend.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class JWTAuthenticationFilterTests {

    @InjectMocks
    JwtAuthenticationFilter authFilter;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    private MockedStatic<JWTUtil> mockedStatic;


    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
        mockedStatic = mockStatic(JWTUtil.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
    }

    @Test
    public void doFilterInternal_ShouldCallTheRestOfTheChain_IfNoTokenIsProvided() throws ServletException, IOException {
        //Arrange
        mockedStatic.when(() -> JWTUtil.resolveToken(request)).thenReturn(null);

        //Act
        authFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_ShouldResolveClaimsIfTokenExistsAndPopulateContextHolder() throws ServletException, IOException {
        // Arrange
        String mockToken = "mockToken";
        Claims mockClaims = Mockito.mock(Claims.class);

        List<Map<String, String>> authoritiesList = new ArrayList<>();
        Map<String, String> authorityMap = new HashMap<>();
        authorityMap.put("authority", "USER_ROLE");
        authoritiesList.add(authorityMap);

        mockedStatic.when(() -> JWTUtil.resolveToken(request)).thenReturn(mockToken);
        mockedStatic.when(() -> JWTUtil.resolveClaims(request)).thenReturn(mockClaims);
        mockedStatic.when(() -> JWTUtil.validateClaims(mockClaims)).thenReturn(true);
        Mockito.when(mockClaims.get("authorities")).thenReturn(authoritiesList);

        // Act
        authFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should be set in SecurityContextHolder");
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_ShouldLeaveContextHolderEmptyIfClaimsInvalid() throws ServletException, IOException {

        //Arrange
        String mockToken = "mockToken";
        Claims mockClaims = Mockito.mock(Claims.class);
        mockedStatic.when(() -> JWTUtil.resolveToken(request)).thenReturn(mockToken);
        mockedStatic.when(() -> JWTUtil.resolveClaims(request)).thenReturn(mockClaims);
        mockedStatic.when(() -> JWTUtil.validateClaims(Mockito.any(Claims.class))).thenReturn(false);

        //Act
        authFilter.doFilterInternal(request, response, filterChain);

        //Assert
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
