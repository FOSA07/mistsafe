package mist_safe.mistsafe;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionFilter())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/signin", "/signup", "/error-page", "/verifyemail");
    }
}
