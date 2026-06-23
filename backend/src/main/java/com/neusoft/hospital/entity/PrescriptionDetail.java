package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 处方明细实体，对应数据库表 prescription_detail
 * 记录处方中的具体药品明细，包括药品、数量、单价、金额及用法用量
 */
@Data
public class PrescriptionDetail {
    /** 处方明细ID，主键自增 */
    private Long id;
    /** 处方ID，关联 prescription.id */
    private Long prescriptionId;
    /** 药品ID，关联 drug.id */
    private Long drugId;
    /** 数量，该药品的开具数量 */
    private Integer quantity;
    /** 单价，药品的单价 */
    private BigDecimal unitPrice;
    /** 金额，数量*单价的合计金额 */
    private BigDecimal amount;
    /** 用法用量，如"口服 每日三次 每次一片" */
    private String usageMethod;
}
