package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Drug {
    private Long id;
    private String drugName;
    private String drugCode;
    private String specification;
    private String unit;
    private String manufacturer;
    private BigDecimal unitPrice;
    private Integer stock;
    private Integer status;
}
