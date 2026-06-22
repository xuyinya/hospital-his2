package com.neusoft.hospital.config;

import com.neusoft.hospital.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login")
                .excludePathPatterns("/api/auth/patient-login")
                .excludePathPatterns("/doc.html", "/swagger-ui/**", "/v3/api-docs/**",
                        "/webjars/**", "/swagger-resources/**", "/favicon.ico");
    }
}
