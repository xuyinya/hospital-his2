package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 处方明细视图对象，用于展示处方中的药品明细
 * 连表查询 prescription_detail 表与 drug 表，在处方管理页面中列出每种药品的名称、规格、数量、金额及用法用量
 */
@Data
public class PrescriptionDetailVO {
    /** 明细ID */
    private Long id;
    /** 所属处方ID，关联 prescription 表 */
    private Long prescriptionId;
    /** 药品ID */
    private Long drugId;
    /** 药品名称（关联 drug 表查询） */
    private String drugName;
    /** 药品规格，如 0.25g*12片、10ml*5支等 */
    private String specification;
    /** 单位，如盒、瓶、袋、支等 */
    private String unit;
    /** 数量 */
    private Integer quantity;
    /** 单价 */
    private BigDecimal unitPrice;
    /** 金额 = 单价 * 数量 */
    private BigDecimal amount;
    /** 用法用量，如口服每日三次每次一片 */
    private String usageMethod;
}
