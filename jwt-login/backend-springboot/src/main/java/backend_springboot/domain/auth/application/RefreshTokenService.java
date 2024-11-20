package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RefreshTokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(User user) {
        long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 7;
        refreshTokenRepository.save(
                user.getId(),
                jwtService.provideRefreshToken(user),
                REFRESH_TOKEN_EXPIRATION,
                convertChronoUnitToTimeUnit(ChronoUnit.DAYS)
        );
    }

    public String getRefreshToken(Long userId) {
        String refreshToken = refreshTokenRepository.findByMemberId(userId);

        if (refreshToken == null || refreshToken.isEmpty()) {
            log.error("리프레시 토큰이 저장소에 존재하지 않습니다.");
            throw new RuntimeException();
        }

        return refreshToken;
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteByMemberId(userId);
    }

    private TimeUnit convertChronoUnitToTimeUnit(ChronoUnit chronoUnit) {
        switch (chronoUnit) {
            case NANOS:
                return TimeUnit.NANOSECONDS;
            case MICROS:
                return TimeUnit.MICROSECONDS;
            case MILLIS:
                return TimeUnit.MILLISECONDS;
            case SECONDS:
                return TimeUnit.SECONDS;
            case MINUTES:
                return TimeUnit.MINUTES;
            case HOURS:
                return TimeUnit.HOURS;
            case DAYS:
                return TimeUnit.DAYS;
            default:
                throw new RuntimeException();
        }
    }
}
