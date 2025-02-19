package backend_springboot.domain.auth.dto.response;

public record RotateTokenResponse(
        String accessToken,
        String refreshToken
) {
    public static RotateTokenResponse of(
            String accessToken,
            String refreshToken
    ) {
        return new RotateTokenResponse(accessToken, refreshToken);
    }
}
