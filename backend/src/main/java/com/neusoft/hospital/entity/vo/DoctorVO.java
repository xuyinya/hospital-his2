package com.neusoft.hospital.entity.vo;

import lombok.Data;

/**
 * 医生视图对象，用于展示医生列表信息
 * 连表查询 doctor 表与 department 表，在医生管理页面展示医生姓名、科室、职称等综合信息
 */
@Data
public class DoctorVO {
    /** 医生ID */
    private Long id;
    /** 医生姓名 */
    private String doctorName;
    /** 所属科室ID */
    private Long deptId;
    /** 科室名称（关联 department 表查询） */
    private String deptName;
    /** 职称，如主任医师、副主任医师、主治医师等 */
    private String title;
    /** 擅长领域 / 专业特长描述 */
    private String specialty;
    /** 状态：0-停诊 1-出诊 */
    private Integer status;
}
