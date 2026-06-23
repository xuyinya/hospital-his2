package com.neusoft.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 患者实体，对应数据库表 patient
 * 记录就诊患者的基本信息，包括姓名、性别、年龄、身份证号、联系方式及登录密码
 */
@Data
public class Patient {
    /** 患者ID，主键自增 */
    private Long id;
    /** 患者姓名 */
    private String patientName;
    /** 性别：0-男，1-女 */
    private Integer gender;
    /** 年龄 */
    private Integer age;
    /** 身份证号，患者唯一证件号码 */
    private String idCard;
    /** 联系电话 */
    private String phone;
    /** 家庭地址 */
    private String address;
    /** 登录密码，患者门户登录使用 */
    private String password;
    /** 创建时间，记录创建时的日期时间 */
    private LocalDateTime createTime;
    /** 更新时间，记录最后修改时的日期时间 */
    private LocalDateTime updateTime;
}
