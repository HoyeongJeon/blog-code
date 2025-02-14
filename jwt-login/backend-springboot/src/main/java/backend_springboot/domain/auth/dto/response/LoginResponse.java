package backend_springboot.domain.auth.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
