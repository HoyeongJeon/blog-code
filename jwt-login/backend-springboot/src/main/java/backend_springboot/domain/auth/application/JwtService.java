package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
@Log4j2
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
        this.REFRESH_EXPIRATION = 10;
    }

    public String provideAccessToken(User user) {
        return provideToken(
                user.getEmail(),
                user.getId(),
                Type.ACCESS,
                ACCESS_EXPIRATION,
                ""
        );
    }

    public String provideRefreshToken(User user, String ip) {
        return provideToken(
                user.getEmail(),
                user.getId(),
                Type.REFRESH,
                REFRESH_EXPIRATION,
                ip
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

    private String provideToken(String email, Long id, Type type, long expiration, String ip) {
        Date expiryDate;
        Map<String, String> claims;
        if (type.equals(Type.ACCESS)) {
            claims = Map.of(
                    "userId", id.toString(),
                    "email", email
            );
            expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS));
            log.info("액세스 만료기간 {}" , expiryDate );
        } else {
            claims = Map.of(
                    "ip", ip
            );
            expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS));
            log.info("리프레시 만료기간 {}" , expiryDate );

        }
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }
}
