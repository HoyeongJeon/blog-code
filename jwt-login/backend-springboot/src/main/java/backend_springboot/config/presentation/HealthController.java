package backend_springboot.config.presentation;

import backend_springboot.config.argumentresolver.AuthorizedMember;
import backend_springboot.config.interceptor.Authorization;
import backend_springboot.domain.auth.domain.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Authorization
    @GetMapping("/health")
    public String health(@AuthorizedMember User user) {
        return "건강합니다. - 스프링부트";
    }
}
