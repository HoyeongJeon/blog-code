package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import backend_springboot.domain.auth.dto.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RotateAccessTokenService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public String rotateAccessToken(String accessToken) {
        Long userIdFromInvalidAccessToken = jwtService.extractUserIdFromInvalidAccessToken(accessToken);
        User user = userRepository.findById(userIdFromInvalidAccessToken).orElseThrow();
        String refreshToken = refreshTokenService.getRefreshToken(user.getId());
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is not exist");
        }
        Long userId = extractUserIdFromRefreshToken(refreshToken);
        validateRefreshToken(userId, refreshToken);

        return jwtService.provideAccessToken(user);
    }

    private Long extractUserIdFromRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            log.error("쿠키에 refreshToken이 존재하지 않습니다.");
            throw new IllegalArgumentException();
        }
        return jwtService.extractMemberId(refreshToken);
    }

    private void validateRefreshToken(Long userId, String refreshToken) {
        String savedRefreshToken = refreshTokenService.getRefreshToken(userId);
        if (!savedRefreshToken.equals(refreshToken)) {
            log.error("리프레시 토큰이 저장소에 존재하지 않습니다.");
            throw new IllegalArgumentException();
        }
    }

}
