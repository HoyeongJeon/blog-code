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
    public void save(Long memberId, String refreshToken, Long expireTime, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(memberId.toString(), refreshToken, expireTime, timeUnit);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String findByMemberId(Long memberId) {
        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            return valueOperations.get(memberId.toString());
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteByMemberId(Long userId) {
        try {
            redisTemplate.delete(userId.toString());
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 오류 : {}", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
