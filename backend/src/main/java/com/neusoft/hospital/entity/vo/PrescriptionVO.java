package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionVO {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime createTime;
    private List<PrescriptionDetailVO> details;
}
