package com.neusoft.hospital.security;

import com.neusoft.hospital.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT工具类
 * <p>
 * 提供JWT令牌的生成、解析和验证功能。
 * 使用HMAC-SHA算法对Token进行签名，确保Token的完整性和防篡改性。
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    /** JWT配置，包含密钥和过期时间 */
    private final JwtConfig jwtConfig;

    /**
     * 获取HMAC-SHA签名密钥
     * <p>
     * 使用配置的secret字符串通过HMAC-SHA算法生成签名密钥。
     * 密钥一致时，生成的Token才能被正确解析验证。
     * </p>
     *
     * @return 签名密钥对象
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT令牌（不含医生ID）
     * <p>
     * 重载方法，适用于管理员等没有关联医生ID的用户。
     * </p>
     *
     * @param userId   用户ID（作为Subject）
     * @param username 用户名
     * @param role     用户角色
     * @return JWT令牌字符串
     */
    public String generateToken(Long userId, String username, String role) {
        return generateToken(userId, username, role, null);
    }

    /**
     * 生成JWT令牌（完整参数）
     * <p>
     * 将用户ID、用户名、角色和关联医生ID作为声明（claims）写入Token，
     * 设置签发时间和过期时间，使用HMAC-SHA算法签名。
     * </p>
     *
     * @param userId   用户ID（作为Subject）
     * @param username 用户名
     * @param role     用户角色（admin/doctor）
     * @param doctorId 关联医生ID（医生角色时有值，管理员为null）
     * @return JWT令牌字符串
     */
    public String generateToken(Long userId, String username, String role, Long doctorId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(String.valueOf(userId))           // 主题：用户ID
                .claim("username", username)               // 自定义声明：用户名
                .claim("role", role)                       // 自定义声明：角色
                .claim("doctorId", doctorId)               // 自定义声明：医生ID
                .issuedAt(now)                             // 签发时间
                .expiration(expiration)                    // 过期时间
                .signWith(getSigningKey())                 // 使用HMAC-SHA签名
                .compact();                                // 压缩为字符串
    }

    /**
     * 解析JWT令牌
     * <p>
     * 使用签名密钥验证Token的完整性和有效期，解析出所有声明（claims）。
     * 如果签名无效或Token已过期，将抛出JwtException。
     * </p>
     *
     * @param token JWT令牌字符串
     * @return 解析后的Claims对象，包含所有声明信息
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // 使用签名密钥验证
                .build()
                .parseSignedClaims(token)     // 解析Token的签名声明
                .getPayload();                // 获取载荷部分
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsername(String token) {
        return parseToken(token).get("username", String.class);
    }

    /**
     * 从Token中获取角色
     *
     * @param token JWT令牌
     * @return 角色标识
     */
    public String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }

    /**
     * 从Token中获取关联医生ID
     * <p>
     * 处理doctorId可能为null或类型为Integer（JSON反序列化时的类型转换）的情况。
     * </p>
     *
     * @param token JWT令牌
     * @return 医生ID（可能为null）
     */
    public Long getDoctorId(String token) {
        Object doctorId = parseToken(token).get("doctorId");
        if (doctorId == null) return null;
        // JSON解析后整数默认转为Integer类型，手动转为Long
        if (doctorId instanceof Integer) return ((Integer) doctorId).longValue();
        return (Long) doctorId;
    }

    /**
     * 验证JWT令牌是否有效
     * <p>
     * 尝试解析Token，如果解析成功（签名正确且未过期）则返回true，
     * 捕获JwtException或IllegalArgumentException异常时返回false。
     * </p>
     *
     * @param token JWT令牌字符串
     * @return true=有效，false=无效或已过期
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token无效：签名错误、过期、格式不正确等
            return false;
        }
    }
}
