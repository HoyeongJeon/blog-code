package backend_springboot.config.configuration;

import backend_springboot.config.argumentresolver.AuthorizedMemberArgumentResolver;
import backend_springboot.config.argumentresolver.ClientIpArgumentResolver;
import backend_springboot.config.interceptor.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    private final AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver;

    private final ClientIpArgumentResolver clientIpArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(clientIpArgumentResolver);
        resolvers.add(authorizedMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/auth/health")  // 인증이 필요한 경로 지정
                .excludePathPatterns("/auth/login", "/auth/signup", "/auth/rotate");  // 인증이 필요없는 경로 제외
    }
}
