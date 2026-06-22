package com.neusoft.hospital.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordVO {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String chiefComplaint;
    private String presentIllness;
    private String diagnosis;
    private String treatmentPlan;
    private LocalDateTime recordTime;
}
