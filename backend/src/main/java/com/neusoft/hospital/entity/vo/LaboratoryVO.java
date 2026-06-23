package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 检验视图对象，用于展示检验记录及患者信息
 * 连表查询 laboratory 表与 patient 表，在检验管理页面展示患者姓名、检验项目、结果及参考范围等综合信息
 */
@Data
public class LaboratoryVO {
    /** 检验记录ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 检验类别，如血液、尿液、生化、免疫等 */
    private String labType;
    /** 检验项目名称，如血常规、肝功能等 */
    private String labName;
    /** 检验结果数值或描述 */
    private String result;
    /** 参考范围（正常值区间） */
    private String referenceRange;
    /** 检验费用 */
    private BigDecimal fee;
    /** 状态：0-未完成 1-已完成 */
    private Integer status;
    /** 检验执行时间 */
    private LocalDateTime labTime;
}
