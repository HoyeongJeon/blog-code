package backend_springboot.domain.auth.exception;

import backend_springboot.config.exception.DomainException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends DomainException {

    public AuthException(String message) {
        super( message, HttpStatus.UNAUTHORIZED);
    }

    public AuthException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
