package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Laboratory {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String labType;
    private String labName;
    private String result;
    private String referenceRange;
    private BigDecimal fee;
    private Integer status;
    private LocalDateTime labTime;
}
