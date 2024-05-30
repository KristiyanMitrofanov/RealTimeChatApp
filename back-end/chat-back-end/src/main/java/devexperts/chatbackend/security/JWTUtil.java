package devexperts.chatbackend.security;

import devexperts.chatbackend.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@PropertySource("classpath:application.properties")
public class JWTUtil {

    private static final long ACCESS_TOKEN_VALIDITY = 60;
    public static final String INV_TOKEN = "Invalid Token";

    @Setter
    private static String secret;
    private static JwtParser parser;
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final Environment environment;

    public JWTUtil(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    private void init() {
        secret = environment.getProperty("jwt.secret");
        parser = Jwts.parser().verifyWith(getSigningKey()).build();
    }

    public static String createToken(String username, List<GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().add("authorities", authorities).subject(username).build();
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_VALIDITY));
        return Jwts.builder()
                .claims(claims)
                .expiration(tokenValidity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    static SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Claims parseJwtClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public static Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException(INV_TOKEN);
        }
    }

    public static String resolveToken(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString != null && queryString.contains("token=")) {
            String[] params = queryString.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    return param.substring(6);
                }
            }
        } else {
            String bearerToken = request.getHeader(TOKEN_HEADER);
            if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
                return bearerToken.substring(TOKEN_PREFIX.length());
            }
        }
        return null;
    }

    public static boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public static String getUsername(Claims claims) {
        return claims.getSubject();
    }

}
