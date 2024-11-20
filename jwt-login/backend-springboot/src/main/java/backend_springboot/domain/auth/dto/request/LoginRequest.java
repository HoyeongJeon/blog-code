package backend_springboot.domain.auth.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
