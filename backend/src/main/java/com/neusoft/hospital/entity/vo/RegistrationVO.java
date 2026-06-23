package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挂号视图对象，用于展示挂号记录及关联的患者、医生、科室信息
 * 连表查询 registration 表、patient 表、doctor 表与 department 表
 * 在挂号管理页面展示患者基本信息、挂号科室、医生、费用及状态等综合信息
 */
@Data
public class RegistrationVO {
    /** 挂号记录ID */
    private Long id;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 性别：0-女 1-男（关联 patient 表查询） */
    private Integer gender;
    /** 年龄（关联 patient 表查询） */
    private Integer age;
    /** 联系电话（关联 patient 表查询） */
    private String phone;
    /** 医生ID */
    private Long doctorId;
    /** 医生姓名（关联 doctor 表查询） */
    private String doctorName;
    /** 医生职称（关联 doctor 表查询） */
    private String title;
    /** 科室ID */
    private Long deptId;
    /** 科室名称（关联 department 表查询） */
    private String deptName;
    /** 挂号类型：普通号 / 专家号 */
    private String regType;
    /** 挂号费 */
    private BigDecimal regFee;
    /** 挂号时间 */
    private LocalDateTime regTime;
    /** 状态：0-待诊 1-已诊 2-取消 */
    private Integer status;
}
