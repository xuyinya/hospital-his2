package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 药品实体，对应数据库表 drug
 * 记录医院药品的详细信息，包括名称、编码、规格、单价、库存及启用状态
 */
@Data
public class Drug {
    /** 药品ID，主键自增 */
    private Long id;
    /** 药品名称 */
    private String drugName;
    /** 药品编码，药品的唯一编码标识 */
    private String drugCode;
    /** 药品规格，如0.25g*12片/盒 */
    private String specification;
    /** 单位，如盒、瓶、袋、支等 */
    private String unit;
    /** 生产厂商，药品的生产企业名称 */
    private String manufacturer;
    /** 单价，药品的销售单价 */
    private BigDecimal unitPrice;
    /** 库存数量，当前库存余量 */
    private Integer stock;
    /** 状态：1-正常，0-停用 */
    private Integer status;
}
