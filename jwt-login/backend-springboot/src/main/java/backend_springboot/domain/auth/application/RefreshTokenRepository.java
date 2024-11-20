package backend_springboot.domain.auth.application;

import java.util.concurrent.TimeUnit;

public interface RefreshTokenRepository {
    void save(Long userId, String refreshToken, Long expireTime, TimeUnit timeUnit);
    String findByMemberId(Long userId);
    void deleteByMemberId(Long userId);
}
