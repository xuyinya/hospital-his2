package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PrescriptionDetailVO {
    private Long id;
    private Long prescriptionId;
    private Long drugId;
    private String drugName;
    private String specification;
    private String unit;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String usageMethod;
}
