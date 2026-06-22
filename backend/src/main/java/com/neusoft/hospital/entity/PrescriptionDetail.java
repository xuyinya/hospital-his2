package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PrescriptionDetail {
    private Long id;
    private Long prescriptionId;
    private Long drugId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String usageMethod;
}
