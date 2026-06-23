package com.neusoft.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统用户实体，对应数据库表 sys_user
 * 记录系统登录用户的信息，包括用户名、密码、真实姓名、角色及关联医生
 */
@Data
public class SysUser {
    /** 用户ID，主键自增 */
    private Long id;
    /** 用户名，登录使用的账号 */
    private String username;
    /** 密码，BCrypt加密存储 */
    private String password;
    /** 真实姓名 */
    private String realName;
    /** 角色：admin-系统管理员，doctor-医生 */
    private String role;
    /** 关联医生ID，关联 doctor.id（医生角色时有效） */
    private Long doctorId;
    /** 状态：1-正常，0-停用 */
    private Integer status;
    /** 创建时间，用户创建时的日期时间 */
    private LocalDateTime createTime;
    /** 更新时间，用户信息最后修改时的日期时间 */
    private LocalDateTime updateTime;
}
