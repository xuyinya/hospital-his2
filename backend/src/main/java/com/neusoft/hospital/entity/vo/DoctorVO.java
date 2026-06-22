package com.neusoft.hospital.entity.vo;

import lombok.Data;

@Data
public class DoctorVO {
    private Long id;
    private String doctorName;
    private Long deptId;
    private String deptName;
    private String title;
    private String specialty;
    private Integer status;
}
