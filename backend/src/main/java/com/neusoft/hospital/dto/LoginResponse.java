package com.neusoft.hospital.dto;

import lombok.Data;

/**
 * 登录响应 DTO（数据传输对象）
 * <p>
 * 封装登录成功后的返回信息，包含JWT令牌和用户基本信息。
 * 前端收到后将token存储在本地，后续请求携带此token进行身份认证。
 * </p>
 */
@Data
public class LoginResponse {
    /** JWT令牌，后续请求需在Authorization头中携带 */
    private String token;
    /** 登录用户名 */
    private String username;
    /** 用户真实姓名 */
    private String realName;
    /** 用户角色（admin=管理员, doctor=医生） */
    private String role;
    /** 身份证号（患者） */
    private String idCard;
    /** 手机号（患者） */
    private String phone;

    /**
     * 构造登录响应对象
     *
     * @param token    JWT令牌
     * @param username 登录用户名
     * @param realName 用户真实姓名
     * @param role     用户角色
     */
    public LoginResponse(String token, String username, String realName, String role) {
        this.token = token;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }
}
