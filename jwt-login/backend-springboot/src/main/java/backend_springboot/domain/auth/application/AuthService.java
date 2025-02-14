package backend_springboot.domain.auth.application;

import backend_springboot.domain.auth.domain.entity.User;
import backend_springboot.domain.auth.dto.UserRepository;
import backend_springboot.domain.auth.dto.request.LoginRequest;
import backend_springboot.domain.auth.dto.request.SignupRequest;
import backend_springboot.domain.auth.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public void signup(SignupRequest signupRequest) {
        User user = User.of(signupRequest.email(), signupRequest.password());
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest, String ip) {
        User existUser = userRepository.findByEmail(loginRequest.email()).orElseThrow();

        if (!existUser.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("Password is not correct");
        }

        String accessToken = jwtService.provideAccessToken(existUser);
        String refreshToken = jwtService.provideRefreshToken(existUser, ip);
        refreshTokenService.saveRefreshToken(existUser, refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }
}
