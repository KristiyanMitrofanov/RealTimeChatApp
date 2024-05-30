package devexperts.chatbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import devexperts.chatbackend.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = JWTUtil.resolveToken(request);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = JWTUtil.resolveClaims(request);
            if (claims != null && JWTUtil.validateClaims(claims)) {
                String username = claims.getSubject();
                List<GrantedAuthority> authorities = extractAuthoritiesFromClaims(claims);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username, "", authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (InvalidTokenException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            mapper.writeValue(response.getWriter(), e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public static List<GrantedAuthority> extractAuthoritiesFromClaims(Claims claims) {
        List<Map<String, String>> rawAuthorities = (List<Map<String, String>>) claims.get("authorities");
        List<GrantedAuthority> authorities = rawAuthorities.stream()
                .map(authMap -> new SimpleGrantedAuthority(authMap.get("authority")))
                .collect(Collectors.toList());
        return authorities;
    }
}
