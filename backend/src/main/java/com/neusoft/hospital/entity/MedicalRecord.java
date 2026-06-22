package com.neusoft.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecord {
    private Long id;
    private Long registrationId;
    private Long patientId;
    private Long doctorId;
    private String chiefComplaint;
    private String presentIllness;
    private String diagnosis;
    private String treatmentPlan;
    private LocalDateTime recordTime;
}
