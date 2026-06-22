package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TreatmentVO {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String patientName;
    private String treatmentName;
    private String treatmentDesc;
    private BigDecimal fee;
    private Integer status;
    private LocalDateTime treatmentTime;
}
