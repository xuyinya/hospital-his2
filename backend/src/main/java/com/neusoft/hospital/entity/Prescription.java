package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Prescription {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private Long doctorId;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime createTime;
}
