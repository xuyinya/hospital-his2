package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Registration {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long deptId;
    private String regType;
    private BigDecimal regFee;
    private LocalDateTime regTime;
    private Integer status;
}
