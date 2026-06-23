package com.neusoft.hospital.entity;

import lombok.Data;

/**
 * 科室实体，对应数据库表 department
 * 用于管理医院各科室的基本信息，包括科室名称、编码、位置及启用状态
 */
@Data
public class Department {
    /** 科室ID，主键自增 */
    private Long id;
    /** 科室名称，如内科、外科、儿科等 */
    private String deptName;
    /** 科室编码，科室的唯一编码标识 */
    private String deptCode;
    /** 科室位置，科室所在的楼层或区域 */
    private String location;
    /** 状态：1-正常，0-停用 */
    private Integer status;
}
