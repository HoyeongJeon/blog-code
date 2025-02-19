package backend_springboot.config.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ErrorMessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
