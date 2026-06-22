package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExaminationVO {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String patientName;
    private String examType;
    private String examName;
    private String result;
    private BigDecimal fee;
    private Integer status;
    private LocalDateTime examTime;
}
