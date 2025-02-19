package backend_springboot.domain.auth.application;

import backend_springboot.config.application.GeoLocationService;
import backend_springboot.domain.auth.domain.entity.User;
import backend_springboot.domain.auth.dto.UserRepository;
import backend_springboot.domain.auth.dto.response.RotateTokenResponse;
import backend_springboot.domain.auth.exception.AuthException;
import backend_springboot.domain.auth.infrastructure.redis.repository.RefreshTokenRedisRepository;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RotateAccessTokenService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final GeoLocationService geoLocationService;

    public RotateTokenResponse rotateToken(String refreshToken, String newIp) throws IOException, GeoIp2Exception {
        isValidRefreshToken(refreshToken);
        String savedIp = jwtService.getPayload(refreshToken).get("ip", String.class);
        if (geoLocationService.checkUserLocation(newIp, savedIp)) {
            refreshTokenService.deleteRefreshToken(refreshToken);
            log.error("100km 밖에서 토큰 재발급을 시도했습니다.");
            // 실제 구현에선 401에러를 반환하도록 한다.
            throw new AuthException("jwt.rotate-request-from-unknown");
        }
        log.info("정상 범위 내에서 토큰 재발급을 시도했습니다.");
        Long userId = refreshTokenRedisRepository.findUserId(refreshToken);
        User user = userRepository.findById(userId).orElseThrow();

        String newAccessToken = jwtService.provideAccessToken(user);
        String newRefreshToken = jwtService.provideRefreshToken(user, newIp);

        refreshTokenService.saveRefreshToken(user, newRefreshToken);

        return RotateTokenResponse.of(newAccessToken, newRefreshToken);
    }

    private void isValidRefreshToken(String refreshToken) {
        if (refreshTokenRedisRepository.findByRefreshToken(refreshToken).isEmpty()) {
            log.error("Redis에 refreshToken이 존재하지 않습니다.");
            throw new IllegalArgumentException();
        }

        jwtService.getPayload(refreshToken);
    }
}
