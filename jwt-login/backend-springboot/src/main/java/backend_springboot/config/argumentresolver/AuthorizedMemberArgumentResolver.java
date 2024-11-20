package backend_springboot.config.argumentresolver;

import backend_springboot.domain.auth.application.JwtService;
import backend_springboot.domain.auth.dto.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import io.jsonwebtoken.Claims;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthorizedMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthorizedMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String accessToken = extractAccessToken(webRequest);
        if (accessToken == null) {
            log.error("토큰이 존재하지 않습니다.");
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        try {
            log.info("AuthorizedMemberArgumentResolver에서 access_token 검증 {}", accessToken);
            // 토큰 검증 및 사용자 정보 추출
            Claims claims = jwtService.getPayload(accessToken);
            log.info("AuthorizedMemberArgumentResolver에서 claims 검증 {}", claims);
            Long userId = jwtService.extractMemberId(accessToken);
            log.info("AuthorizedMemberArgumentResolver에서 userId 검증 {}", userId);

            return userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    private String extractAccessToken(NativeWebRequest request) {
        HttpServletRequest httpRequest = request.getNativeRequest(HttpServletRequest.class);
        if (httpRequest != null) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                return Arrays.stream(cookies)
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            }
        }
        return null;
    }
}
