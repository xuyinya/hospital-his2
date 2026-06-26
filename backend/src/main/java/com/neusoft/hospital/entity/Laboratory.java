package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 检验实体，对应数据库表 laboratory
 * 记录患者的检验项目信息，包括检验类型、项目名称、结果及参考范围、费用及完成状态
 */
@Data
public class Laboratory {
    /** 检验ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 检验类型，如血常规、尿常规、肝功能等 */
    private String labType;
    /** 检验项目名称 */
    private String labName;
    /** 检验结果，检验得出的数值或结论 */
    private String result;
    /** 参考范围，检验项目的正常参考值范围 */
    private String referenceRange;
    /** 费用，检验项目的收费金额 */
    private BigDecimal fee;
    /** 状态：0-待检验，1-已完成 */
    private Integer status;
    /** 检验时间，检验完成的日期时间 */
    private LocalDateTime labTime;

    // ---- 以下为JOIN查询时填充的关联字段（非数据库列） ----
    /** 患者姓名，关联 patient 表 */
    private String patientName;
}
