package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方视图对象，用于展示处方主信息及关联明细
 * 连表查询 prescription 表、patient 表与 doctor 表，同时封装处方明细列表
 * 在处方管理页面展示患者姓名、医生姓名、总金额、状态及详细的药品清单
 */
@Data
public class PrescriptionVO {
    /** 处方ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 开方医生ID */
    private Long doctorId;
    /** 开方医生姓名（关联 doctor 表查询） */
    private String doctorName;
    /** 处方总金额 */
    private BigDecimal totalAmount;
    /** 状态：0-未取药 1-已取药 */
    private Integer status;
    /** 开方时间 */
    private LocalDateTime createTime;
    /** 处方明细列表，包含每种药品的名称、规格、数量、金额及用法用量 */
    private List<PrescriptionDetailVO> details;
}
