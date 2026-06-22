package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RegistrationVO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Integer gender;
    private Integer age;
    private String phone;
    private Long doctorId;
    private String doctorName;
    private String title;
    private Long deptId;
    private String deptName;
    private String regType;
    private BigDecimal regFee;
    private LocalDateTime regTime;
    private Integer status;
}
