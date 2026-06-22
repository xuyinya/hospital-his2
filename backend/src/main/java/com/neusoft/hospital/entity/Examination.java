package com.neusoft.hospital.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Examination {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String examType;
    private String examName;
    private String result;
    private BigDecimal fee;
    private Integer status;
    private LocalDateTime examTime;
}
