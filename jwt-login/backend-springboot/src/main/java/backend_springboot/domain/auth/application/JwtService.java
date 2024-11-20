package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Getter
    @RequiredArgsConstructor
    public enum Type {
        ACCESS("access"), REFRESH("refresh");
        private final String type;
    }

    private final SecretKey SECRET_KEY;
    private final long ACCESS_EXPIRATION;
    private final long REFRESH_EXPIRATION;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_EXPIRATION = 5;
        this.REFRESH_EXPIRATION = 60 * 60 * 24 * 7;
    }

    public String provideAccessToken(User user) {
        return provideToken(
                user.getEmail(),
                user.getId(),
                Type.ACCESS,
                ACCESS_EXPIRATION
        );
    }


    public String provideRefreshToken(User user) {
        return provideToken(
                user.getEmail(),
                user.getId(),
                Type.REFRESH,
                REFRESH_EXPIRATION
        );
    }

    public Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long extractMemberId(String token) {
        Claims claims = getPayload(token);
        return Long.parseLong(claims.get("userId", String.class));
    }

    public void validateTokenExpiration(String token) {
        Claims claims = getPayload(token);
        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Token has expired");
        }
    }

    private String provideToken(String email, Long id, Type type, long expiration) {
        Date expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS));
        return Jwts.builder()
                .claims(Map.of(
                        "userId", id.toString(),
                        "email", email,
                        "type", type.getType()))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Long extractUserIdFromInvalidAccessToken(String invalidAccessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(invalidAccessToken)
                    .getPayload();
            String userId = claims.get("userId", String.class);
            return Long.parseLong(userId);
        } catch (ExpiredJwtException e) {
            String userId = e.getClaims().get("userId", String.class);
            return Long.parseLong(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract userId from invalid token", e);
        }
    }
}
