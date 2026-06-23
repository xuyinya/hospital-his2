package com.neusoft.hospital.dto;

import lombok.Data;

/**
 * 管理员/医生登录请求 DTO（数据传输对象）
 * <p>
 * 封装用户登录时提交的用户名和密码，接收前端JSON请求体。
 * 验证通过后由AuthController返回LoginResponse。
 * </p>
 */
@Data
public class LoginRequest {
    /** 登录用户名 */
    private String username;
    /** 登录密码（明文） */
    private String password;
}
