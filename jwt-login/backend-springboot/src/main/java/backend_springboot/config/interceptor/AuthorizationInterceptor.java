package backend_springboot.config.interceptor;

import backend_springboot.domain.auth.application.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = extractAccessToken(request);
        if (accessToken == null) {
            log.error("토큰이 존재하지 않습니다.");
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        try {
            jwtService.validateTokenExpiration(accessToken);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    log.info("access_token: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
