package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收费实体，对应数据库表 payment
 * 记录患者的费用收取信息，包括收费类型、总金额、支付方式、支付时间及支付状态
 */
@Data
public class Payment {
    /** 收费ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 收费类型：挂号费、药费、检查费、检验费、处置费 */
    private String paymentType;
    /** 总金额，该笔收费的总金额 */
    private BigDecimal totalAmount;
    /** 支付方式：现金、微信、支付宝、医保 */
    private String paymentMethod;
    /** 支付时间，完成支付的日期时间 */
    private LocalDateTime paymentTime;
    /** 状态：0-未支付，1-已支付，2-已退款 */
    private Integer status;
}
