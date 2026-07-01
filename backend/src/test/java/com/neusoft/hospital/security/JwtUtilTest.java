package com.neusoft.hospital.security;

import com.neusoft.hospital.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtil 单元测试
 * <p>
 * 验证 JWT 令牌的生成、解析、验证全流程。
 * 不依赖 Spring 容器，手动构造 JwtConfig 实例。
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    /** 固定密钥，必须 ≥ 256 bit 以满足 HMAC-SHA 要求 */
    private static final String SECRET = "NeusoftHospitalHIS2026SecretKeyForJWTTokenGeneration";
    /** 过期时间：1 小时 */
    private static final long EXPIRATION = 3_600_000L;

    @BeforeEach
    void setUp() {
        JwtConfig config = new JwtConfig();
        config.setSecret(SECRET);
        config.setExpiration(EXPIRATION);
        jwtUtil = new JwtUtil(config);
    }

    @Test
    @DisplayName("生成令牌 → 解析回读，所有声明应一致")
    void testGenerateAndParse() {
        // 生成包含所有字段的令牌
        String token = jwtUtil.generateToken(1L, "admin", "admin", 999L);

        assertAll("令牌声明解析回读",
                () -> assertEquals(1L, jwtUtil.getUserId(token)),
                () -> assertEquals("admin", jwtUtil.getUsername(token)),
                () -> assertEquals("admin", jwtUtil.getRole(token)),
                () -> assertEquals(999L, jwtUtil.getDoctorId(token))
        );
    }

    @Test
    @DisplayName("生成的令牌应通过验证")
    void testValidateToken() {
        String token = jwtUtil.generateToken(2L, "doctor", "doctor", 5L);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("无效令牌应被拒")
    void testInvalidToken() {
        assertAll("各类无效令牌均应返回 false",
                () -> assertFalse(jwtUtil.validateToken("")),
                () -> assertFalse(jwtUtil.validateToken("garbage.token.here")),
                () -> assertFalse(jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.signature_invalid"))
        );
    }

    @Test
    @DisplayName("doctorId 为 null 时应返回 null")
    void testDoctorIdNull() {
        // 生成不含 doctorId 的令牌
        String token = jwtUtil.generateToken(3L, "nurse", "admin");
        assertNull(jwtUtil.getDoctorId(token));
    }

    @Test
    @DisplayName("患者角色令牌：角色声明正确")
    void testPatientToken() {
        String token = jwtUtil.generateToken(10L, "张三", "patient");
        assertEquals("patient", jwtUtil.getRole(token));
        assertEquals("张三", jwtUtil.getUsername(token));
        assertEquals(10L, jwtUtil.getUserId(token));
    }
}
