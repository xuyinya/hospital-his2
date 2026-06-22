package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentVO {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String patientName;
    private String paymentType;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private Integer status;
}
