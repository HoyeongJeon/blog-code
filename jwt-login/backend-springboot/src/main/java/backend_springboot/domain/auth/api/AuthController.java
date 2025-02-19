package backend_springboot.domain.auth.api;

import backend_springboot.config.argumentresolver.ClientIp;
import backend_springboot.domain.auth.application.AuthService;
import backend_springboot.domain.auth.application.RotateAccessTokenService;
import backend_springboot.domain.auth.dto.request.LoginRequest;
import backend_springboot.domain.auth.dto.request.SignupRequest;
import backend_springboot.domain.auth.dto.response.LoginResponse;
import backend_springboot.domain.auth.dto.response.RotateTokenResponse;
import backend_springboot.domain.auth.exception.AuthException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RotateAccessTokenService rotateAccessTokenService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok().body("회원가입 되었습니다!");
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@ClientIp String ip, @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest, ip);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", loginResponse.accessToken())
                .httpOnly(true)
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    @PostMapping("/rotate")
    public ResponseEntity<?> rotate(@CookieValue("refresh_token") String refreshToken, @ClientIp String ip) throws IOException, GeoIp2Exception {
        RotateTokenResponse rotateTokens = rotateAccessTokenService.rotateToken(refreshToken, ip);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", rotateTokens.accessToken())
                .httpOnly(true)
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", rotateTokens.refreshToken())
                .httpOnly(true)
                .path("/")
                .build();

        URI location = URI.create("/auth/rotate");

        return ResponseEntity.created(location)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("토큰 재발급에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie logoutAccessTokenCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie logoutRefreshTokenCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, logoutAccessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, logoutRefreshTokenCookie.toString())
                .body("로그아웃 성공!");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        throw new AuthException("jwt.invalid-token");
    }
}
