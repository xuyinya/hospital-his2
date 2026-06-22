package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;

public interface MedicalRecordService {
    void add(MedicalRecord medicalRecord);
    void update(MedicalRecord medicalRecord);
    MedicalRecord getById(Long id);
    MedicalRecord getByRegistrationId(Long registrationId);
    PageResult<MedicalRecordVO> list(Long registrationId, Long patientId, Long doctorId, String patientName, Integer page, Integer size);
}
