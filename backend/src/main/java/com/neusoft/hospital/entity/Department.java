package com.neusoft.hospital.entity;

import lombok.Data;

@Data
public class Department {
    private Long id;
    private String deptName;
    private String deptCode;
    private String location;
    private Integer status;
}
