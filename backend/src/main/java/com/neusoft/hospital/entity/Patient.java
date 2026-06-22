package com.neusoft.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Patient {
    private Long id;
    private String patientName;
    private Integer gender;
    private Integer age;
    private String idCard;
    private String phone;
    private String address;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
