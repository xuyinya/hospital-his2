package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 检查视图对象，用于展示检查记录及患者信息
 * 连表查询 examination 表与 patient 表，在检查管理页面展示患者姓名、检查项目、结果等综合信息
 */
@Data
public class ExaminationVO {
    /** 检查记录ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 检查类别，如影像学、内镜、超声等 */
    private String examType;
    /** 检查项目名称，如胸部X光、腹部B超等 */
    private String examName;
    /** 检查结果 / 报告内容 */
    private String result;
    /** 检查费用 */
    private BigDecimal fee;
    /** 状态：0-未完成 1-已完成 */
    private Integer status;
    /** 检查执行时间 */
    private LocalDateTime examTime;
}
