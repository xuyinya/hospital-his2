package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 处置视图对象，用于展示处置记录及患者信息
 * 连表查询 treatment 表与 patient 表，在处置管理页面展示患者姓名、处置项目、费用及完成状态等综合信息
 */
@Data
public class TreatmentVO {
    /** 处置记录ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 处置名称，如换药、清创、注射等 */
    private String treatmentName;
    /** 处置描述 / 操作说明 */
    private String treatmentDesc;
    /** 处置费用 */
    private BigDecimal fee;
    /** 状态：0-未完成 1-已完成 */
    private Integer status;
    /** 处置执行时间 */
    private LocalDateTime treatmentTime;
}
