package com.neusoft.hospital.entity;

import lombok.Data;

/**
 * 医生实体，对应数据库表 doctor
 * 记录医院医生的基本信息，包括姓名、所属科室、职称、专长及在岗状态
 */
@Data
public class Doctor {
    /** 医生ID，主键自增 */
    private Long id;
    /** 医生姓名 */
    private String doctorName;
    /** 所属科室ID，关联 department.id */
    private Long deptId;
    /** 职称，如主任医师、副主任医师、主治医师等 */
    private String title;
    /** 专长，医生的专业特长描述 */
    private String specialty;
    /** 状态：1-正常，0-停用 */
    private Integer status;
}
