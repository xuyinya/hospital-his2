package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 病历视图对象，用于展示病历详情
 * 连表查询 medical_record 表、patient 表与 doctor 表，在病历管理页面展示患者信息、诊断结论及治疗方案等综合信息
 */
@Data
public class MedicalRecordVO {
    /** 病历ID */
    private Long id;
    /** 挂号ID，关联 registration 表 */
    private Long registrationId;
    /** 患者ID */
    private Long patientId;
    /** 患者姓名（关联 patient 表查询） */
    private String patientName;
    /** 医生ID */
    private Long doctorId;
    /** 医生姓名（关联 doctor 表查询） */
    private String doctorName;
    /** 主诉：患者自行描述的主要症状 */
    private String chiefComplaint;
    /** 现病史：当前疾病的发生、发展及诊疗经过 */
    private String presentIllness;
    /** 诊断意见：医生作出的疾病诊断 */
    private String diagnosis;
    /** 治疗方案：医生制定的治疗计划和用药建议 */
    private String treatmentPlan;
    /** 病历记录时间 */
    private LocalDateTime recordTime;
}
