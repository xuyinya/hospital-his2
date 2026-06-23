package com.neusoft.hospital.security;

import com.neusoft.hospital.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 * <p>
 * 实现HandlerInterceptor接口，在请求到达Controller之前进行Token校验。
 * 验证通过后将用户信息注入请求属性，供后续业务代码直接使用。
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    /** JWT工具类，提供Token的解析和验证功能 */
    private final JwtUtil jwtUtil;
    /** Jackson JSON转换器，用于将错误响应序列化为JSON */
    private final ObjectMapper objectMapper;

    /**
     * 请求预处理方法
     * <p>
     * 对每个进入 /api/** 的请求执行Token验证：
     * <ol>
     *   <li>OPTIONS预检请求直接放行</li>
     *   <li>从Authorization头提取Bearer Token</li>
     *   <li>验证Token有效性，无效则返回401</li>
     *   <li>通过后解析用户信息存入request属性</li>
     * </ol>
     * </p>
     *
     * @param request  当前HTTP请求
     * @param response 当前HTTP响应
     * @param handler  请求处理器
     * @return true表示放行，false表示拦截
     * @throws Exception 处理过程中的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 请求放行（浏览器跨域预检请求）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头中获取Authorization字段
        String authHeader = request.getHeader("Authorization");
        // 检查Authorization头是否存在且以"Bearer "开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401状态码
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("未登录或token已过期")));
            return false;
        }

        // 截取"Bearer "后面的Token字符串
        String token = authHeader.substring(7);
        // 验证Token是否有有效
        if (!jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("token无效或已过期")));
            return false;
        }

        // 将用户信息存入请求属性，后续Controller可从request中获取当前用户
        request.setAttribute("userId", jwtUtil.getUserId(token));
        request.setAttribute("username", jwtUtil.getUsername(token));
        request.setAttribute("role", jwtUtil.getRole(token));
        request.setAttribute("doctorId", jwtUtil.getDoctorId(token));
        return true;
    }
}
