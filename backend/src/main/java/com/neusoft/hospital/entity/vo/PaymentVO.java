package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收费视图对象，用于展示收费记录及患者信息
 * 连表查询 payment 表与 patient 表，在收费管理页面展示患者姓名、收费类型、金额及支付状态等综合信息
 */
@Data
public class PaymentVO {
    /** 收费记录ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 收费类型：挂号费 / 药费 / 检查费 / 检验费 / 处置费 */
    private String paymentType;
    /** 总金额 */
    private BigDecimal totalAmount;
    /** 支付方式：现金 / 微信 / 支付宝 / 医保卡 */
    private String paymentMethod;
    /** 支付时间 */
    private LocalDateTime paymentTime;
    /** 状态：0-待支付 1-已支付 */
    private Integer status;
}
