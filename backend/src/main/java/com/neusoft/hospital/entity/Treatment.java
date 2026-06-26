package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 处置实体，对应数据库表 treatment
 * 记录患者的处置治疗信息，包括处置项目名称、描述、费用及完成状态
 */
@Data
public class Treatment {
    /** 处置ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 处置项目名称，如换药、清创、注射等 */
    private String treatmentName;
    /** 处置描述，处置的具体内容说明 */
    private String treatmentDesc;
    /** 费用，处置项目的收费金额 */
    private BigDecimal fee;
    /** 状态：0-待处置，1-已完成 */
    private Integer status;
    /** 处置时间，处置完成的日期时间 */
    private LocalDateTime treatmentTime;

    // ---- 以下为JOIN查询时填充的关联字段（非数据库列） ----
    /** 患者姓名，关联 patient 表 */
    private String patientName;
}
