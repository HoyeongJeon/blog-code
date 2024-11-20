package backend_springboot.domain.auth.dto.request;

public record SignupRequest(
        String email,
        String password
) {
}
