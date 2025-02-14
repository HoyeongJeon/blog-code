package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(User user, String refreshToken) {
        long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 7;
        refreshTokenRepository.save(
                refreshToken,
                user.getId(),
                REFRESH_TOKEN_EXPIRATION,
                convertChronoUnitToTimeUnit(ChronoUnit.DAYS)
        );
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteRefreshToken(refreshToken);
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
