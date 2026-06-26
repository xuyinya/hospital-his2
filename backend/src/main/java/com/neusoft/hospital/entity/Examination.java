package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 检查实体，对应数据库表 examination
 * 记录患者的检查项目信息，包括检查类型、项目名称、结果、费用及完成状态
 */
@Data
public class Examination {
    /** 检查ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 检查类型，如CT、X光、B超、心电图等 */
    private String examType;
    /** 检查项目名称 */
    private String examName;
    /** 检查结果，医生填写的检查结论 */
    private String result;
    /** 费用，检查项目的收费金额 */
    private BigDecimal fee;
    /** 状态：0-待检查，1-已完成 */
    private Integer status;
    /** 检查时间，检查完成的日期时间 */
    private LocalDateTime examTime;

    // ---- 以下为JOIN查询时填充的关联字段（非数据库列） ----
    /** 患者姓名，关联 patient 表 */
    private String patientName;
}
