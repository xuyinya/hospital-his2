package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.mapper.PatientMapper;
import com.neusoft.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientMapper patientMapper;

    @Override
    public void add(Patient patient) {
        patientMapper.insert(patient);
    }

    @Override
    public void update(Patient patient) {
        patientMapper.update(patient);
    }

    @Override
    public void delete(Long id) {
        patientMapper.deleteById(id);
    }

    @Override
    public Patient getById(Long id) {
        return patientMapper.selectById(id);
    }

    @Override
    public Patient getByIdCard(String idCard) {
        return patientMapper.selectByIdCard(idCard);
    }

    @Override
    public PageResult<Patient> list(String patientName, Long doctorId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Patient> rows = patientMapper.selectList(patientName, doctorId, offset, size);
        Long total = patientMapper.selectCount(patientName, doctorId);
        return new PageResult<>(total, rows);
    }
}
