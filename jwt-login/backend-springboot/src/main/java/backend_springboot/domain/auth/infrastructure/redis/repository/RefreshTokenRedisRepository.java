package backend_springboot.domain.auth.infrastructure.redis.repository;

import backend_springboot.domain.auth.application.RefreshTokenRepository;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Log4j2
public class RefreshTokenRedisRepository implements RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String refreshToken, Long memberId, Long expireTime, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(refreshToken, memberId.toString(), expireTime, timeUnit);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String findByRefreshToken(String refreshToken) {
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            return valueOperations.get(refreshToken);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Long findUserId(String refreshToken) {
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String userId = valueOperations.get(refreshToken);
            if (userId == null) {
                log.warn("리프레시 토큰에 해당하는 사용자 없음: {}", refreshToken);
                return null;
            }
            return Long.parseLong(userId);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        try {
            redisTemplate.delete(refreshToken);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
