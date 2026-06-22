package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Treatment {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String treatmentName;
    private String treatmentDesc;
    private BigDecimal fee;
    private Integer status;
    private LocalDateTime treatmentTime;
}
