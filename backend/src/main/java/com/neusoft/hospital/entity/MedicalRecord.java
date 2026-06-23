package com.neusoft.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 病历实体，对应数据库表 medical_record
 * 记录患者的门诊病历信息，包括主诉、现病史、诊断结果、治疗方案及记录时间
 */
@Data
public class MedicalRecord {
    /** 病历ID，主键自增 */
    private Long id;
    /** 挂号ID，关联 registration.id */
    private Long registrationId;
    /** 患者ID，关联 patient.id */
    private Long patientId;
    /** 医生ID，关联 doctor.id */
    private Long doctorId;
    /** 主诉，患者自述的主要症状及持续时间 */
    private String chiefComplaint;
    /** 现病史，患者本次发病的详细经过 */
    private String presentIllness;
    /** 诊断结果，医生做出的疾病诊断 */
    private String diagnosis;
    /** 治疗方案，医生制定的治疗计划 */
    private String treatmentPlan;
    /** 记录时间，病历创建的日期时间 */
    private LocalDateTime recordTime;
}
