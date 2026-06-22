package com.neusoft.hospital.entity;

import lombok.Data;

@Data
public class Doctor {
    private Long id;
    private String doctorName;
    private Long deptId;
    private String title;
    private String specialty;
    private Integer status;
}
