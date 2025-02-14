package backend_springboot.domain.auth.application;

import java.util.concurrent.TimeUnit;

public interface RefreshTokenRepository {
    void save(String refreshToken, Long userId, Long expireTime, TimeUnit timeUnit);
    String findByRefreshToken(String refreshToken);
    void deleteRefreshToken(String refreshToken);
    Long findUserId(String refreshToken);
}
