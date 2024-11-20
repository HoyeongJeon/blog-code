package backend_springboot.domain.auth.api;

import backend_springboot.config.argumentresolver.AuthorizedMember;
import backend_springboot.config.interceptor.Authorization;
import backend_springboot.domain.auth.application.AuthService;
import backend_springboot.domain.auth.application.RotateAccessTokenService;
import backend_springboot.domain.auth.domain.entity.User;
import backend_springboot.domain.auth.dto.request.LoginRequest;
import backend_springboot.domain.auth.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RotateAccessTokenService rotateAccessTokenService;

    @PostMapping("signup")
    public void signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String accessToken = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/rotate")
    public ResponseEntity<?> rotate(@CookieValue("access_token") String accessToken) {
        String newAccessToken = rotateAccessTokenService.rotateAccessToken(accessToken);
        ResponseCookie cookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .path("/")
                .build();

        URI location = URI.create("/auth/rotate");

        return ResponseEntity.created(location)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @Authorization
    @GetMapping("/health")
    public String health(@AuthorizedMember User user) {
        return "건강합니다. - 스프링부트";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("access_token", "") // 값을 비움
                .httpOnly(true)
                .path("/")
                .maxAge(0) // 쿠키를 즉시 만료
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("로그아웃 성공!");
    }
}
