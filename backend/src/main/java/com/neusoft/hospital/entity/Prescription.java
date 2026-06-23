package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 处方实体，对应数据库表 prescription
 * 记录医生为患者开具的处方信息，包括所属挂号和患者、开单医生、总金额及取药状态
 */
@Data
public class Prescription {
    /** 处方ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 医生ID，关联 doctor.id */
    private Long doctorId;
    /** 总金额，处方中所有药品的总价 */
    private BigDecimal totalAmount;
    /** 状态：0-未取药，1-已取药 */
    private Integer status;
    /** 开单时间，处方创建的日期时间 */
    private LocalDateTime createTime;
}
