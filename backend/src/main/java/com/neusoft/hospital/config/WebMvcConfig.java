package com.neusoft.hospital.config;

import com.neusoft.hospital.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * <p>
 * 注册自定义的JWT拦截器，实现对API接口的登录认证保护。
 * 配置拦截路径和放行路径，保证登录接口和文档页面无需认证即可访问。
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /** JWT认证拦截器，用于校验请求Token */
    private final JwtInterceptor jwtInterceptor;

    /**
     * 注册拦截器
     * <p>
     * 拦截所有 /api/** 路径的请求，但放行登录接口和Swagger文档资源，
     * 确保未登录用户可以访问登录页和接口文档。
     * </p>
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有API请求
                .addPathPatterns("/api/**")
                // 放行管理员登录接口
                .excludePathPatterns("/api/auth/login")
                // 放行患者登录接口
                .excludePathPatterns("/api/auth/patient-login")
                // 放行Swagger/Knife4j文档相关资源路径
                .excludePathPatterns("/doc.html", "/swagger-ui/**", "/v3/api-docs/**",
                        "/webjars/**", "/swagger-resources/**", "/favicon.ico");
    }
}
