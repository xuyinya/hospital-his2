package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;
import com.neusoft.hospital.mapper.MedicalRecordMapper;
import com.neusoft.hospital.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public void add(MedicalRecord medicalRecord) {
        medicalRecordMapper.insert(medicalRecord);
    }

    @Override
    public void update(MedicalRecord medicalRecord) {
        medicalRecordMapper.update(medicalRecord);
    }

    @Override
    public MedicalRecord getById(Long id) {
        return medicalRecordMapper.selectById(id);
    }

    @Override
    public MedicalRecord getByRegistrationId(Long registrationId) {
        return medicalRecordMapper.selectByRegistrationId(registrationId);
    }

    @Override
    public PageResult<MedicalRecordVO> list(Long registrationId, Long patientId, Long doctorId, String patientName, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<MedicalRecordVO> rows = medicalRecordMapper.selectMedicalRecordVO(registrationId, patientId, doctorId, patientName, offset, size);
        Long total = medicalRecordMapper.selectMedicalRecordVOCount(registrationId, patientId, doctorId, patientName);
        return new PageResult<>(total, rows);
    }
}
