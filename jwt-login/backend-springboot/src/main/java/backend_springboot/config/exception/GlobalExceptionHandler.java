package backend_springboot.config.exception;

import backend_springboot.config.utils.ErrorMessageUtil;
import backend_springboot.domain.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessageUtil errorMessageUtil;

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<String> handleAuthException(DomainException e) {
        HttpStatus httpStatus = e.getHttpStatus() != null ? e.getHttpStatus() : HttpStatus.UNAUTHORIZED;
        String errorMessage = errorMessageUtil.getMessage(e.getMessage());
        System.out.println("errorMessage = " + errorMessage);
        return ResponseEntity
                .status(httpStatus)
                .body(errorMessage);
    }
}
