package com.neusoft.hospital.dto;

import lombok.Data;

/**
 * 患者登录请求 DTO（数据传输对象）
 * <p>
 * 封装患者登录时提交的身份证号和密码。
 * 患者通过患者门户系统登录，使用身份证号作为唯一标识。
 * </p>
 */
@Data
public class PatientLoginRequest {
    /** 患者身份证号，作为患者登录的唯一标识 */
    private String idCard;
    /** 登录密码（明文） */
    private String password;
}
