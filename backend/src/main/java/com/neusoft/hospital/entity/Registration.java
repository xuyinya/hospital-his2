package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挂号实体，对应数据库表 registration
 * 记录患者的挂号信息，包括患者、医生、科室、挂号类型、费用及就诊状态
 */
@Data
public class Registration {
    /** 挂号ID，主键自增 */
    private Long id;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 医生ID，关联 doctor.id */
    private Long doctorId;
    /** 科室ID，关联 department.id */
    private Long deptId;
    /** 挂号类型：普通号、专家号 */
    private String regType;
    /** 挂号费，挂号时收取的费用 */
    private BigDecimal regFee;
    /** 挂号时间，挂号的日期时间 */
    private LocalDateTime regTime;
    /** 状态：0-待诊，1-已诊，2-取消 */
    private Integer status;

    // ---- 以下为JOIN查询时填充的关联字段（非数据库列） ----
    /** 患者姓名，关联 patient 表 */
    private String patientName;
    /** 性别，关联 patient 表 */
    private Integer gender;
    /** 年龄，关联 patient 表 */
    private Integer age;
    /** 手机号，关联 patient 表 */
    private String phone;
    /** 医生姓名，关联 doctor 表 */
    private String doctorName;
    /** 职称，关联 doctor 表 */
    private String title;
    /** 科室名称，关联 department 表 */
    private String deptName;
}
